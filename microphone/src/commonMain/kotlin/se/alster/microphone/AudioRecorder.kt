package se.alster.microphone

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

interface AudioRecorder {
    val audioStream: Flow<ByteArray>
    fun startRecording()
    fun stopRecording()
}

@Composable
expect fun rememberAudioRecorder(): AudioRecorder