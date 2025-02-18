package se.alster.microphone

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioPCMBuffer

class AudioRecorderImpl : AudioRecorder {
    private val audioEngine = AVAudioEngine()
    private val inputNode = audioEngine.inputNode
    private val bus: ULong = 0u

    @OptIn(ExperimentalForeignApi::class)
    override val audioStream: Flow<ByteArray> = callbackFlow {
        val format = inputNode.inputFormatForBus(bus)
        val frameSize = 1024  // Number of frames per buffer (adjustable)

        inputNode.installTapOnBus(bus, frameSize.toUInt(), format) { buffer, _ ->
            val data = buffer!!.toByteArray()
            trySend(data)
        }

        audioEngine.prepare()
        audioEngine.startAndReturnError(null)

        awaitClose {
            inputNode.removeTapOnBus(bus)
            audioEngine.stop()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun startRecording() {
        if (!audioEngine.running) {
            audioEngine.startAndReturnError(null)
        }
    }

    override fun stopRecording() {
        audioEngine.stop()
        inputNode.removeTapOnBus(bus)
    }
}

// Extension function to convert audio buffer to ByteArray
@OptIn(ExperimentalForeignApi::class)
private fun AVAudioPCMBuffer.toByteArray(): ByteArray {
    val audioBuffer = floatChannelData?.get(0) ?: return ByteArray(0)
    val byteArray = ByteArray(frameLength.toInt() * 2) // 16-bit PCM (2 bytes per sample)

    for (i in 0 until frameLength.toInt()) {
        val sample = (audioBuffer[i] * Short.MAX_VALUE).toInt().toShort() // Convert float (-1.0 to 1.0) to PCM 16-bit
        byteArray[i * 2] = (sample.toInt() and 0xFF).toByte()
        byteArray[i * 2 + 1] = ((sample.toInt() shr 8) and 0xFF).toByte()
    }
    return byteArray
}

@Composable
actual fun rememberAudioRecorder(): AudioRecorder {
    return remember { AudioRecorderImpl() }
}