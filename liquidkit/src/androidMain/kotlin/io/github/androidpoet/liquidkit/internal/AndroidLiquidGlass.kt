@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.internal

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.drawBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.blur
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.lens
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.vibrancy
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.highlight.Highlight

/*
 * Android Liquid Glass renderer backed by vendored source from:
 * https://github.com/Kyant0/AndroidLiquidGlass
 */
internal fun Modifier.androidLiquidGlass(
    shape: Shape,
    style: LiquidGlassStyle,
): Modifier =
    composed {
        // Use a layer backdrop from the composition local if a scaffold has set one up,
        // otherwise fall back to an empty canvas backdrop (lens / highlight still apply).
        val layerCapture = LocalLiquidLayerBackdrop.current
        val emptyBackdrop = rememberCanvasBackdrop {}
        val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else emptyBackdrop

        drawBackdrop(
            backdrop = backdrop,
            shape = { shape },
            effects = {
                vibrancy()
                blur(style.androidBlurRadius.toPx())
                lens(style.androidRefractionHeight.toPx(), style.androidRefractionAmount.toPx())
            },
            highlight = { Highlight.Default },
            onDrawSurface = { drawRect(style.containerColor) },
        )
    }

private val LiquidGlassStyle.androidBlurRadius: Dp
    get() =
        when (this) {
            LiquidGlassStyle.Surface -> 16.dp
            LiquidGlassStyle.NavigationBar -> 18.dp
            LiquidGlassStyle.Control -> 14.dp
            else -> (cornerRadius * 0.72f).coerceIn(8.dp, 22.dp)
        }

private val LiquidGlassStyle.androidRefractionHeight: Dp
    get() =
        when (this) {
            LiquidGlassStyle.Surface -> 10.dp
            LiquidGlassStyle.NavigationBar -> 14.dp
            LiquidGlassStyle.Control -> 8.dp
            else -> (cornerRadius * 0.48f).coerceIn(6.dp, 16.dp)
        }

// Refraction amount is roughly 2× height for a pronounced glass-edge distortion.
private val LiquidGlassStyle.androidRefractionAmount: Dp
    get() =
        when (this) {
            LiquidGlassStyle.Surface -> 20.dp
            LiquidGlassStyle.NavigationBar -> 28.dp
            LiquidGlassStyle.Control -> 16.dp
            else -> (cornerRadius * 0.96f).coerceIn(12.dp, 32.dp)
        }
