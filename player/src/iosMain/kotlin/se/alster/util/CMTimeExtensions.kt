package se.alster.util

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeAdd
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMake
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalForeignApi::class)
operator fun CValue<CMTime>.plus(duration: Duration): CValue<CMTime> {
    return CMTimeAdd(CMTimeMake(duration.inWholeMilliseconds, 1000), this)
}

@OptIn(ExperimentalForeignApi::class)
fun CValue<CMTime>.toDuration(): Duration {
    if (CMTimeGetSeconds(this).isNaN()) {
        return Duration.INFINITE
    }
    return CMTimeGetSeconds(this).toDuration(DurationUnit.SECONDS)
}
