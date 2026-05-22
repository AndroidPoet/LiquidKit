package io.github.androidpoet.liquidkit.internal

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.drawBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.blur
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.lens
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop

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
            blur(style.androidBlurRadius.toPx())
            lens(style.androidRefractionHeight.toPx(), style.androidRefractionHeight.toPx())
        },
        onDrawSurface = { drawRect(style.containerColor) },
    )
        .border(1.dp, Color.White.copy(alpha = 0.34f), shape)
}

private val LiquidGlassStyle.androidBlurRadius: Dp
    get() = when (this) {
        LiquidGlassStyle.Surface -> 16.dp
        LiquidGlassStyle.NavigationBar -> 18.dp
        LiquidGlassStyle.Control -> 14.dp
        else -> (cornerRadius * 0.72f).coerceIn(8.dp, 22.dp)
    }

private val LiquidGlassStyle.androidRefractionHeight: Dp
    get() = when (this) {
        LiquidGlassStyle.Surface -> 10.dp
        LiquidGlassStyle.NavigationBar -> 14.dp
        LiquidGlassStyle.Control -> 8.dp
        else -> (cornerRadius * 0.48f).coerceIn(6.dp, 16.dp)
    }
