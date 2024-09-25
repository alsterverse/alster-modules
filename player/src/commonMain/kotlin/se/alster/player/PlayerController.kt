package se.alster.player

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

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