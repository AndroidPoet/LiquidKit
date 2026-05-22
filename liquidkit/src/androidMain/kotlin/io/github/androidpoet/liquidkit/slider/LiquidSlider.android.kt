package io.github.androidpoet.liquidkit.slider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidSlider

@Composable
internal actual fun PlatformLiquidSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val backdrop = rememberCanvasBackdrop {
        drawRect(style.containerColor)
    }

    LiquidSlider(
        value = { value },
        onValueChange = { if (enabled) onValueChange(it) },
        valueRange = valueRange,
        visibilityThreshold = (valueRange.endInclusive - valueRange.start) / 1_000f,
        backdrop = backdrop,
        modifier = modifier.alpha(if (enabled) 1f else 0.42f),
        enabled = enabled,
        activeTrackColor = style.selectedContentColor.copy(alpha = 0.58f),
        inactiveTrackColor = style.contentColor.copy(alpha = 0.16f),
        thumbColor = style.containerColor.copy(alpha = 0.92f),
    )
}
