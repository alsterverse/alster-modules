package se.alster

import androidx.compose.runtime.Composable
import se.alster.player.rememberPlayerController
import se.alster.player.ui.VideoView
import se.alster.theme.AppTheme

@Composable
internal fun App() = AppTheme {
    val player = rememberPlayerController()
    player.loadVideo("http://localhost:8000/output/output.m3u8")
    player.setPlayOnReady(true)
    VideoView(
        showControls = true,
        playerProvider = player.playerProvider,
    )
}
