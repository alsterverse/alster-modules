package se.alster.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val ProgressFlowResolution = 100.milliseconds

class PlayerControllerAndroid(context: Context) : PlayerController {
    private val exoPlayer = ExoPlayer.Builder(context).build()

    override val playerProvider: PlayerProvider = object : PlayerProvider {
        override val player: ExoPlayer = exoPlayer
    }

    override val currentProgress: Flow<Duration> = flow {
        while (currentCoroutineContext().isActive) {
            emit(exoPlayer.currentPosition.milliseconds)
            delay(ProgressFlowResolution)
        }
    }

    private val _videoDuration = MutableStateFlow<Duration?>(null)
    override val videoDuration: Flow<Duration> = _videoDuration
        .filterNotNull()
        .filter { it.isPositive() }

    private val _hasEnded = MutableStateFlow(false)
    override val hasVideoEnded: Flow<Boolean> = _hasEnded.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: Flow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: Flow<Boolean> = _isLoading.asStateFlow()

    override fun isReadyToPlay(): Boolean {
        return exoPlayer.playbackState == Player.STATE_READY
    }

    override fun setPlayOnReady(shouldPlay: Boolean) {
        exoPlayer.playWhenReady = shouldPlay
    }

    private val playerListener = object : Player.Listener {
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            _videoDuration.value = exoPlayer.duration.milliseconds
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            _hasEnded.value = playbackState == Player.STATE_ENDED
            if (playbackState == Player.STATE_READY) {
                _isLoading.value = false
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            _isPlaying.value = isPlaying
            if (isPlaying) {
                _isLoading.value = false
            }
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            _isLoading.value = isLoading
        }
    }

    init {
        exoPlayer.addListener(playerListener)
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                println("Video player error: $error")
            }
        })
    }

    override fun release() {
        exoPlayer.release()
    }

    override fun loadVideo(url: String) {
        exoPlayer.setMediaItem(MediaItem.fromUri(url))
        exoPlayer.prepare()
    }

    fun prepare() {
        exoPlayer.prepare()
    }

    override fun play() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun scrub(delta: Duration) {
        _isLoading.value = true
        exoPlayer.seekTo(exoPlayer.currentPosition + delta.inWholeMilliseconds)
    }

    override fun seekTo(position: Duration) {
        _isLoading.value = true
        exoPlayer.seekTo(position.inWholeMilliseconds)
    }
}
