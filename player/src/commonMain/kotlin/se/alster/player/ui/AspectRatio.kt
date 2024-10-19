package se.alster.player.ui

/**
 * The aspect ratio of the video.
 *
 * [ScaleToFit] - The video is scaled to fit inside the view. The aspect ratio is preserved.
 * [ScaleToFill] - The video is scaled to fill the view. The aspect ratio is preserved.
 * [FillStretch] - The video is stretched to fill the view. The aspect ratio is not preserved.
 */
enum class AspectRatio {
    /**
     * The video is scaled to fit inside the view. The aspect ratio is preserved.
     */
    ScaleToFit,

    /**
     * The video is scaled to fill the view. The aspect ratio is preserved.
     */
    ScaleToFill,

    /**
     * The video is stretched to fill the view. The aspect ratio is not preserved.
     */
    FillStretch,
}