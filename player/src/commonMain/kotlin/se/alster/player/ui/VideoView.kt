package se.alster.player.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import se.alster.player.PlayerProvider

/**
 * A composable that displays a video player. The player is provided by the [playerProvider].
 * Note: The [VideoView] does not handle the lifecycle of the player. The player should be
 * released when it is no longer needed.
 *
 * Example usage:
 * ```kotlin
 * val playerProvider = rememberPlayerProvider()
 * LaunchedEffect(playerProvider) {
 *    playerProvider.loadVideo("https://example.com/video.mp4")
 *    playerProvider.play()
 * }
 * VideoView(
 *    modifier = Modifier.fillMaxSize(),
 *    playerProvider = playerProvider
 *    showControls = true
 * )
 * ```
 */
@Composable
fun VideoView(
    modifier: Modifier = Modifier.fillMaxSize(),
    showControls: Boolean = true,
    aspectRatio: AspectRatio = AspectRatio.ScaleToFill,
    playerProvider: PlayerProvider,
) = InternalVideoView(modifier, showControls, aspectRatio, playerProvider)

@Composable
internal expect fun InternalVideoView(
    modifier: Modifier,
    showControls: Boolean,
    aspectRatio: AspectRatio,
    playerProvider: PlayerProvider
)
