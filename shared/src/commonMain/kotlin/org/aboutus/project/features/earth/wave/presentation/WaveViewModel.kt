package org.aboutus.project.features.earth.wave.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.aboutus.project.core.data.SupaServices
import org.aboutus.project.core.mapStateToWaveCase
import org.aboutus.project.features.earth.wave.domain.WaveCalculator
import org.aboutus.project.features.earth.words.data.MessageState
import org.aboutus.project.features.earth.domain.toEarthError

class WaveViewModel(
    private val wordsRepository: SupaServices
) : ViewModel() {

    private val _state = MutableStateFlow(WaveState())
    val state: StateFlow<WaveState> = _state.asStateFlow()

    private var refreshJob: Job? = null

    fun fetchWaveStatsForCountry(countryCode: String?) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (true) {
                refreshFromTable(countryCode)
                delay(5_000)
            }
        }
    }

    private suspend fun refreshFromTable(countryCode: String?) {
        _state.update { it.copy(isLoading = it.stats.isEmpty(), statsError = null) }
        val globalResult = runCatching { wordsRepository.fetchCountryStats(null) }
        val scopedResult = if (countryCode == null) globalResult
        else runCatching { wordsRepository.fetchCountryStats(countryCode.uppercase()) }

        scopedResult
            .onSuccess { rawStats ->
                val stats = rawStats.normalizedStats()
                val globalStats = globalResult.getOrNull()?.normalizedStats() ?: _state.value.globalStats
                val totalsByState = stats
                    .groupBy { it.state }
                    .mapValues { (_, entries) -> entries.sumOf { it.count } }
                val dominant = totalsByState.maxByOrNull { it.value }
                val totalVotes = totalsByState.values.sum()
                val countryCount = stats.map { it.country_code }.distinct().size
                val dominantShare = if (totalVotes == 0L) 0f else dominant?.value?.toFloat()?.div(totalVotes) ?: 0f
                val intensity = 0.12f + dominantShare * 0.28f
                val params = WaveCalculator.calculateParams(intensity)

                _state.update {
                    it.copy(
                        scopeCountryCode = countryCode,
                        stats = stats,
                        globalStats = globalStats,
                        totalVotes = totalVotes,
                        participatingCountries = countryCount,
                        dominantState = dominant?.key,
                        statsError = null,
                        isLoading = false,
                        intensity = intensity,
                        waveCase = dominant?.key?.let(::mapStateToWaveCase) ?: it.waveCase,
                        speedFactor = params.speedFactor,
                        amplitudeFactor = params.amplitudeFactor
                    )
                }
            }
            .onFailure { error ->
                _state.update { it.copy(statsError = error.toEarthError(), isLoading = false) }
            }
    }

    private fun List<org.aboutus.project.features.earth.words.data.CountryStatDto>.normalizedStats() =
        filter { it.state != "ALL" }
            .map { stat -> stat.copy(state = MessageState.fromValue(stat.state).value) }
}
