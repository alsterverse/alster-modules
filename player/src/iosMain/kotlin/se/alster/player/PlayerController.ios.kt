package se.alster.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberPlayerController(): PlayerController {
    val playerController = remember { PlayerControllerIOS() }
    return playerController
}