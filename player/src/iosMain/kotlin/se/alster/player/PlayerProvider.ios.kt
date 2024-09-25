package se.alster.player

import platform.AVFoundation.AVPlayer

actual interface PlayerProvider {
    val player: AVPlayer
}