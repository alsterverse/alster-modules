package se.alster.player

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration
import se.alster.player.ui.VideoView

/**
 * A controller for a video player. The controller is responsible for loading and playing videos.
 * The controller also provides information about the current state of the video player. The controller
 * is not responsible for rendering the video player. The rendering is done by the [VideoView].
 */
interface PlayerController {
    val playerProvider: PlayerProvider
    val currentProgress: Flow<Duration>
    val videoDuration: Flow<Duration>
    val hasVideoEnded: Flow<Boolean>
    val isPlaying: Flow<Boolean>
    val isLoading: Flow<Boolean>
    fun isReadyToPlay(): Boolean
    fun setPlayOnReady(shouldPlay: Boolean)
    fun release()
    fun loadVideo(url: String)
    fun play()
    fun pause()
    fun scrub(delta: Duration)
    fun seekTo(position: Duration)
}

@Composable
expect fun rememberPlayerController(): PlayerController