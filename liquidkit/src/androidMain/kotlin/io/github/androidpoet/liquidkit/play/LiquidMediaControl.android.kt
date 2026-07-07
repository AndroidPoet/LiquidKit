@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.play

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
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidSlider

@Composable
internal actual fun PlatformLiquidMediaControl(
    isPlaying: Boolean,
    onPlayPauseToggle: (Boolean) -> Unit,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier,
    progressRange: ClosedFloatingPointRange<Float>,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    Row(
        modifier = modifier.alpha(if (enabled) 1f else 0.42f),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LiquidButton(
            onClick = { if (enabled) onPlayPauseToggle(!isPlaying) },
            backdrop = backdrop,
            isInteractive = enabled,
            surfaceColor = style.selectedContainerColor,
        ) {
            Box(
                modifier = Modifier.size(width = 22.dp, height = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                // ▶ / ❚❚ as text glyphs to avoid an icon dependency.
                BasicText(
                    text = if (isPlaying) "❚❚" else "▶",
                    style =
                        TextStyle(
                            color = style.selectedContentColor,
                            fontSize = if (isPlaying) 16.sp else 20.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                )
            }
        }

        LiquidSlider(
            value = { progress },
            onValueChange = { if (enabled) onProgressChange(it) },
            valueRange = progressRange,
            visibilityThreshold = (progressRange.endInclusive - progressRange.start) / 1_000f,
            backdrop = backdrop,
            modifier = Modifier.weight(1f),
        )
    }
}
