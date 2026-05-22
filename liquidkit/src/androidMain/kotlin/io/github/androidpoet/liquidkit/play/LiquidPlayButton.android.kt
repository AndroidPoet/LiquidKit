package io.github.androidpoet.liquidkit.play

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidButton

@Composable
internal actual fun PlatformLiquidPlayButton(
    playing: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val backdrop = rememberCanvasBackdrop {
        drawRect(style.containerColor)
    }

    LiquidButton(
        onClick = { if (enabled) onPlayingChange(!playing) },
        backdrop = backdrop,
        modifier = modifier
            .size(56.dp)
            .alpha(if (enabled) 1f else 0.42f),
        enabled = enabled,
        surfaceColor = style.selectedContainerColor,
    ) {
        Canvas(Modifier.size(20.dp)) {
            if (playing) {
                val barWidth = size.width * 0.28f
                val barHeight = size.height * 0.82f
                val top = (size.height - barHeight) / 2f
                drawRoundRect(
                    color = style.selectedContentColor,
                    topLeft = Offset(size.width * 0.18f, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(2.dp.toPx()),
                )
                drawRoundRect(
                    color = style.selectedContentColor,
                    topLeft = Offset(size.width * 0.54f, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(2.dp.toPx()),
                )
            } else {
                val playPath = Path().apply {
                    moveTo(size.width * 0.28f, size.height * 0.16f)
                    lineTo(size.width * 0.28f, size.height * 0.84f)
                    lineTo(size.width * 0.82f, size.height * 0.50f)
                    close()
                }
                drawPath(playPath, style.selectedContentColor)
            }
        }
    }
}
