@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.slider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
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
    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    LiquidSlider(
        value = { value },
        onValueChange = { if (enabled) onValueChange(it) },
        valueRange = valueRange,
        visibilityThreshold = (valueRange.endInclusive - valueRange.start) / 1_000f,
        backdrop = backdrop,
        modifier = modifier.alpha(if (enabled) 1f else 0.42f),
    )
}
