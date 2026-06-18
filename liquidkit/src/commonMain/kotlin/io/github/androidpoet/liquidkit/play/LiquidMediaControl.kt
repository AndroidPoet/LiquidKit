package io.github.androidpoet.liquidkit.play

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle

/**
 * A Liquid Glass media transport: a play/pause toggle button paired with a scrubber/progress slider.
 *
 * On Android the transport is rendered with the LiquidKit glass backdrop engine; on iOS it uses a
 * genuine native `UISlider` scrubber plus a glass `UIButton` (SF Symbol play/pause) via UIKit
 * interop so it adopts authentic system Liquid Glass on iOS 26.
 *
 * @param isPlaying whether the transport is currently in the playing state.
 * @param onPlayPauseToggle invoked with the new playing state when the toggle is tapped.
 * @param progress the current scrubber progress, coerced into [progressRange].
 * @param onProgressChange invoked while the user scrubs.
 * @param modifier the [Modifier] to apply to this transport.
 * @param progressRange the inclusive range the [progress] is coerced into.
 * @param enabled whether the transport responds to user input.
 * @param style the [LiquidGlassStyle] used to tint the glass surfaces.
 */
@Composable
public fun LiquidMediaControl(
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    progressRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
) {
    require(progressRange.start < progressRange.endInclusive) {
        "LiquidMediaControl requires a non-empty progressRange."
    }

    PlatformLiquidMediaControl(
        isPlaying = isPlaying,
        onPlayPauseToggle = onPlayPauseToggle,
        progress = progress.coerceIn(progressRange),
        onProgressChange = onProgressChange,
        modifier = modifier,
        progressRange = progressRange,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidMediaControl(
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier,
    progressRange: ClosedFloatingPointRange<Float>,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
