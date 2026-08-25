package org.aboutus.project.core.microphone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.hyochan.audio.AudioRecorderPlayer
import io.github.hyochan.audio.createAudioRecorderPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.aboutus.project.core.permission.microphone.RecordingStatus
import java.io.File

class JvmAudioRecorderController(
    private val audioRecorderPlayerFactory: () -> AudioRecorderPlayer = ::createAudioRecorderPlayer,
) : AudioRecorderController {
    private val _status = MutableStateFlow(RecordingStatus.IDLE)

    private val audioRecorderPlayer: AudioRecorderPlayer by lazy {
        audioRecorderPlayerFactory().also { player ->
            player.addPlaybackListener { progress ->
            if (progress.duration > 0L && progress.currentPosition >= progress.duration) {
                _status.value = RecordingStatus.RECORDED
            }
        }
        }
    }

    override fun getStatus(): StateFlow<RecordingStatus> = _status.asStateFlow()

    override suspend fun startRecording() {
        withContext(Dispatchers.IO) {
            runCatching { audioRecorderPlayer.stopPlaying() }
            val path = File.createTempFile("prayer_voice_", ".m4a").absolutePath
            audioRecorderPlayer.startRecording(path)
                .onSuccess { _status.value = RecordingStatus.RECORDING }
                .onFailure {
                    _status.value = RecordingStatus.ERROR
                    throw it
                }
        }
    }

    override suspend fun stopRecording(): String = withContext(Dispatchers.IO) {
        audioRecorderPlayer.stopRecording()
            .onSuccess { path ->
                _status.value = if (path.isNotBlank()) RecordingStatus.RECORDED else RecordingStatus.IDLE
            }
            .onFailure { _status.value = RecordingStatus.ERROR }
            .getOrThrow()
    }

    override suspend fun play(audioPath: String) {
        withContext(Dispatchers.IO) {
            audioRecorderPlayer.stopPlaying()
            audioRecorderPlayer.startPlaying(audioPath)
                .onSuccess { _status.value = RecordingStatus.PLAYING }
                .onFailure {
                    _status.value = RecordingStatus.ERROR
                    throw it
                }
        }
    }

    override suspend fun stopPlayback() {
        withContext(Dispatchers.IO) {
            audioRecorderPlayer.stopPlaying()
                .onSuccess {
                    if (_status.value == RecordingStatus.PLAYING) {
                        _status.value = RecordingStatus.RECORDED
                    }
                }
                .onFailure {
                    _status.value = RecordingStatus.ERROR
                    throw it
                }
        }
    }
}

@Composable
actual fun rememberAudioRecorderController(): AudioRecorderController {
    return remember { JvmAudioRecorderController() }
}
