@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.stepper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidButton

@Composable
internal actual fun PlatformLiquidStepper(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    val canDecrement = enabled && value > valueRange.start
    val canIncrement = enabled && value < valueRange.endInclusive

    Row(
        modifier = modifier.alpha(if (enabled) 1f else 0.42f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LiquidButton(
            onClick = {
                if (canDecrement) {
                    onValueChange((value - step).coerceIn(valueRange))
                }
            },
            backdrop = backdrop,
            isInteractive = canDecrement,
            surfaceColor = style.containerColor,
            modifier = Modifier.alpha(if (canDecrement) 1f else 0.42f),
        ) {
            StepperGlyph(text = "−", color = style.selectedContentColor)
        }

        LiquidButton(
            onClick = {
                if (canIncrement) {
                    onValueChange((value + step).coerceIn(valueRange))
                }
            },
            backdrop = backdrop,
            isInteractive = canIncrement,
            surfaceColor = style.containerColor,
            modifier = Modifier.alpha(if (canIncrement) 1f else 0.42f),
        ) {
            StepperGlyph(text = "+", color = style.selectedContentColor)
        }
    }
}

@Composable
private fun StepperGlyph(text: String, color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier.size(width = 20.dp, height = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style =
                TextStyle(
                    color = color,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                ),
        )
    }
}
