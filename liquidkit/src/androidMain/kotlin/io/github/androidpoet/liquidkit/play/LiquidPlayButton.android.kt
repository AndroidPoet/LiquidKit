package io.github.androidpoet.liquidkit.play

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
        modifier = modifier.alpha(if (enabled) 1f else 0.42f),
        enabled = enabled,
        tint = style.selectedContainerColor,
    ) {
        BasicText(
            text = if (playing) "Pause" else "Play",
            style = TextStyle(
                color = style.selectedContentColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}
