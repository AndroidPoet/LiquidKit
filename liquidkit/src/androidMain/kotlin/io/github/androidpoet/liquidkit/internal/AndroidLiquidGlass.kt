package io.github.androidpoet.liquidkit.internal

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import io.github.androidpoet.liquidkit.LiquidGlassStyle

/*
 * Android Liquid Glass renderer backed by vendored source copied from:
 * https://github.com/Kyant0/AndroidLiquidGlass
 */
internal fun Modifier.androidLiquidGlass(
    shape: Shape,
    style: LiquidGlassStyle,
): Modifier = composed {
    val backdrop = rememberCanvasBackdrop {
        drawRect(style.containerColor)
    }

    drawBackdrop(
        backdrop = backdrop,
        shape = { shape },
        effects = {
            blur(style.blurRadius.toPx())
            lens(style.refractionHeight.toPx(), style.refractionHeight.toPx())
        },
        onDrawSurface = { drawRect(style.containerColor) },
    )
        .border(1.dp, Color.White.copy(alpha = 0.34f), shape)
}
