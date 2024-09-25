package se.alster.player

import androidx.media3.common.Player

actual interface PlayerProvider {
    val player: Player
}
