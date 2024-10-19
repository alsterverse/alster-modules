package se.alster.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberPlayerController(): PlayerController {
    val context = LocalContext.current
    val playerController = remember { PlayerControllerAndroid(context) }
    return playerController
}