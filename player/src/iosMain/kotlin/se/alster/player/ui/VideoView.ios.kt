package se.alster.player.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.AVFoundation.AVLayerVideoGravity
import platform.AVFoundation.AVLayerVideoGravityResize
import platform.AVFoundation.AVLayerVideoGravityResizeAspect
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVKit.AVPlayerViewController
import se.alster.player.PlayerProvider

@Composable
internal actual fun InternalVideoView(
    modifier: Modifier,
    showControls: Boolean,
    aspectRatio: AspectRatio,
    playerProvider: PlayerProvider
) {
    val player = playerProvider.player

    val avPlayerViewController = remember { AVPlayerViewController() }
    UIKitView(
        factory = {
            avPlayerViewController.player = player
            avPlayerViewController.videoGravity = aspectRatio.toVideoGravity()
            avPlayerViewController.showsPlaybackControls = showControls

            avPlayerViewController.view
        },
        modifier = modifier,
    )
}

private fun AspectRatio.toVideoGravity(): AVLayerVideoGravity = when (this) {
    AspectRatio.ScaleToFit -> AVLayerVideoGravityResizeAspect
    AspectRatio.ScaleToFill -> AVLayerVideoGravityResizeAspectFill
    AspectRatio.FillStretch -> AVLayerVideoGravityResize
}
