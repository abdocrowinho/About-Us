package org.aboutus.project.features.earth.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.aboutus.project.core.data.SupaServices
import org.aboutus.project.features.earth.words.data.MessageState
import org.aboutus.project.features.earth.words.data.WordPayload
import kotlin.math.PI
import kotlin.random.Random

class EarthWordsViewModel(
    private val realtimeService: SupaServices
) : ViewModel() {

    private val _wordsState = MutableStateFlow<List<FloatingWord>>(emptyList())
    val wordsState: StateFlow<List<FloatingWord>> = _wordsState.asStateFlow()
    private var activeCountryCode: String? = null
    private var allWords: List<FloatingWord> = emptyList()

    init {
        subscribeToRealtimeWords()
        startParticleEngine()
    }

    private fun subscribeToRealtimeWords() {
        viewModelScope.launch {
            try {
                realtimeService.connect()
                realtimeService.listenToWordsStream().collect { payload ->
                    onNewWordReceived(payload = payload, globeRadius = 180f)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onNewWordReceived(payload: WordPayload, globeRadius: Float) {
        allWords = allWords.let { current ->
            if (current.any { it.id == payload.id }) {
                current
            } else {
                val newWord = FloatingWord(
                    id = payload.id,
                    word = payload.text,
                    fullSentence = payload.fullSentence,
                    sentence = payload.state.value,
                    countryCode = payload.countryCode,
                    state = payload.state.number.toString(),
                    angleRad = Random.nextFloat() * (2f * PI.toFloat()),
                    currentDistance = globeRadius + 10f,
                    maxDistance = globeRadius + 400f,
                    speed = 0.9f + Random.nextFloat() * 1.1f,
                )
                current + newWord
            }
        }
        publishVisibleWords()
    }

    fun setScope(countryCode: String?) {
        activeCountryCode = countryCode
        publishVisibleWords()
    }

    private fun publishVisibleWords() {
        _wordsState.value = allWords.filter { word ->
            activeCountryCode == null || word.countryCode.equals(activeCountryCode, ignoreCase = true)
        }
    }

    fun sendSentenceStream(
        fullText: String,
        state: MessageState,
        countryCode: String = "EG",
        lat: Double = 30.0444,
        lng: Double = 31.2357
    ) {
        if (fullText.isBlank()) return

        viewModelScope.launch {
            // تقطيع النص إلى كلمات
            val allWords = fullText.trim().split("\\s+".toRegex())

            // تجميع كل كلمتين متتاليتين مع بعض (تقدر تخليها .chunked(1) لو عاوز كلمة كلمة)
            val wordChunks = allWords.chunked(2).map { it.joinToString(" ") }

            wordChunks.forEachIndexed { index, chunkText ->
                val payload = WordPayload(
                    id = "${Random.nextLong()}_$index",
                    text = chunkText,
                    fullSentence = fullText, // الجملة الكلية محفوظة داخل كل عنصر للـ Dialog
                    state = state,
                    countryCode = countryCode,
                    lat = lat,
                    lng = lng,
                    timestamp = Clock.System.now().toEpochMilliseconds()
                )

                // 1. إضافة الكلمة فوراً في الـ UI المحلي (Optimistic Updates)
                onNewWordReceived(payload = payload, globeRadius = 180f)

                // 2. إرسال الكلمة المقطعة عبر الـ Realtime
                try {
                    realtimeService.sendWord(payload)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // فاصل زمني بين كل قطعة والثانية لإعطاء التأثير المتتابع
                delay(180)
            }
        }
    }

    private fun startParticleEngine() {
        viewModelScope.launch {
            while (true) {
                delay(32) // ~30 FPS

                allWords = allWords.mapNotNull { word ->
                        val newDistance = word.currentDistance + word.speed
                        val totalDistance = word.maxDistance - (180f + 10f)
                        val currentProgress = (newDistance - (180f + 10f)) / totalDistance

                        val newAlpha = if (currentProgress > 0.8f) {
                            (1f - ((currentProgress - 0.8f) / 0.2f)).coerceIn(0f, 1f)
                        } else {
                            1f
                        }

                        if (newDistance >= word.maxDistance || newAlpha <= 0f) {
                            null
                        } else {
                            word.copy(
                                currentDistance = newDistance,
                                alpha = newAlpha
                            )
                        }
                    }
                publishVisibleWords()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            realtimeService.disconnect()
        }
    }
}
