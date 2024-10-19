package se.alster.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import se.alster.player.PlayerProvider
import se.alster.player.R

@OptIn(UnstableApi::class)
@Composable
internal actual fun InternalVideoView(
    modifier: Modifier,
    showControls: Boolean,
    aspectRatio: AspectRatio,
    playerProvider: PlayerProvider
) {
    val player = playerProvider.player
    val currentView = LocalView.current as ViewGroup

    AndroidView(
        modifier = modifier.clipToBounds(),
        factory = { ctx ->
            (LayoutInflater.from(ctx).inflate(
                R.layout.video_texture_view,
                currentView,
                false
            ) as PlayerView)
                .also {
                    it.resizeMode = aspectRatio.toResizeMode()
                    it.useController = showControls
                    it.player = player
                }
        },
    )
}

@OptIn(UnstableApi::class)
private fun AspectRatio.toResizeMode(): Int = when (this) {
    AspectRatio.ScaleToFit -> AspectRatioFrameLayout.RESIZE_MODE_FIT
    AspectRatio.ScaleToFill -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    AspectRatio.FillStretch -> AspectRatioFrameLayout.RESIZE_MODE_FILL
}
