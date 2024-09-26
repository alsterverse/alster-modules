package se.alster.player

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapNotNull
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerItemStatusReadyToPlay
import platform.AVFoundation.AVPlayerStatusReadyToPlay
import platform.AVFoundation.AVPlayerStatusUnknown
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.playImmediatelyAtRate
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTimeMake
import platform.CoreMedia.kCMTimeZero
import platform.Foundation.NSKeyValueObservingOptionNew
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.Foundation.addObserver
import platform.Foundation.removeObserver
import platform.darwin.NSObject
import platform.foundation.NSKeyValueObservingProtocol
import se.alster.util.plus
import se.alster.util.toDuration
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val PeriodicTimeObserverTimeScale = 600

@OptIn(ExperimentalForeignApi::class)
class PlayerControllerIOS : PlayerController {
    val avPlayer = AVPlayer()

    @OptIn(ExperimentalForeignApi::class)
    private val periodicTimeObserverForInterval = avPlayer.addPeriodicTimeObserverForInterval(
        CMTimeMake(1, PeriodicTimeObserverTimeScale),
        null,
    ) { time ->
        _positionFlow.value = time.toDuration()
    }

    @OptIn(ExperimentalForeignApi::class)
    private val currentItemStatusObserver = object : NSObject(), NSKeyValueObservingProtocol {
        override fun observeValueForKeyPath(
            keyPath: String?,
            ofObject: Any?,
            change: Map<Any?, *>?,
            context: COpaquePointer?
        ) {
            if (avPlayer.currentItem?.status == AVPlayerItemStatusReadyToPlay) {
                _videoDuration.value = avPlayer.currentItem?.duration?.toDuration()
            }
        }
    }

    private val playerItemDidPlayToEndTimeNotification =
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = avPlayer.currentItem,
            queue = null
        ) { _ ->
            _hasEnded.value = true
        }

    private val playerTimeControlObserver = object : NSObject(), NSKeyValueObservingProtocol {
        override fun observeValueForKeyPath(
            keyPath: String?,
            ofObject: Any?,
            change: Map<Any?, *>?,
            context: COpaquePointer?
        ) {
            _isLoading.value =
                avPlayer.timeControlStatus == AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate
            _isPlaying.value = avPlayer.timeControlStatus == AVPlayerTimeControlStatusPlaying
        }
    }

    override val currentProgress: Flow<Duration>
        get() = _positionFlow.mapNotNull { it }

    private val _videoDuration = MutableStateFlow<Duration?>(null)
    override val videoDuration: Flow<Duration> = _videoDuration.filterNotNull()

    private val _hasEnded = MutableStateFlow(false)
    override val hasVideoEnded: Flow<Boolean> = _hasEnded.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: Flow<Boolean> = _isPlaying.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    @OptIn(FlowPreview::class)
    override val isLoading: Flow<Boolean> = _isLoading.asStateFlow()
        .debounce(1.milliseconds.inWholeMilliseconds) // debounce to prevent flickering from loading to playing

    override fun isReadyToPlay(): Boolean {
        return if (avPlayer.status == AVPlayerStatusUnknown) {
            avPlayer.currentItem?.status == AVPlayerItemStatusReadyToPlay
        } else {
            avPlayer.status == AVPlayerStatusReadyToPlay
        }
    }

    override fun setPlayOnReady(shouldPlay: Boolean) {
        avPlayer.playImmediatelyAtRate(if (shouldPlay) 1.0f else 0.0f)
    }

    private val _positionFlow = MutableStateFlow<Duration?>(null)

    override val playerProvider: PlayerProvider = object : PlayerProvider {
        override val player: AVPlayer = avPlayer
    }

    init {
        avPlayer.addObserver(
            playerTimeControlObserver,
            "timeControlStatus",
            NSKeyValueObservingOptionNew,
            null
        )
    }

    override fun release() {
        avPlayer.pause()
        avPlayer.removeTimeObserver(periodicTimeObserverForInterval)
        avPlayer.currentItem?.removeObserver(currentItemStatusObserver, "status")
        avPlayer.removeObserver(playerTimeControlObserver, "timeControlStatus")
        NSNotificationCenter.defaultCenter.removeObserver(playerItemDidPlayToEndTimeNotification)
    }

    override fun loadVideo(url: String) {
        avPlayer.replaceCurrentItemWithPlayerItem(
            AVPlayerItem.playerItemWithURL(
                NSURL.URLWithString(url)!!
            )
        )
        avPlayer.currentItem?.addObserver(
            observer = currentItemStatusObserver,
            forKeyPath = "status",
            options = NSKeyValueObservingOptionNew,
            context = null
        )
        avPlayer.pause()
    }

    override fun play() {
        _hasEnded.value = false
        avPlayer.play()
    }

    override fun pause() {
        avPlayer.pause()
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun scrub(delta: Duration) {
        _hasEnded.value = false
        avPlayer.seekToTime(
            time = avPlayer.currentTime() + delta,
            toleranceBefore = kCMTimeZero.readValue(),
            toleranceAfter = kCMTimeZero.readValue(),
        )
    }

    override fun seekTo(position: Duration) {
        _hasEnded.value = false
        avPlayer.seekToTime(
            time = CMTimeMake(0, timescale = 60000) + position,
            toleranceBefore = kCMTimeZero.readValue(),
            toleranceAfter = kCMTimeZero.readValue(),
        )
    }
}
