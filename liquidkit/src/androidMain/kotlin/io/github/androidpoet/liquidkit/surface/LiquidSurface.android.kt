@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.surface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.layerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.drawBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.blur
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.lens
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.vibrancy
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.highlight.Highlight
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.shadow.InnerShadow
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.shadow.Shadow
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.utils.InteractiveHighlight
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh

@Composable
internal actual fun PlatformLiquidSurface(
    modifier: Modifier,
    shape: Shape,
    cornerRadius: Dp,
    tint: Color,
    interactive: Boolean,
    style: LiquidGlassStyle,
    content: @Composable BoxScope.() -> Unit,
) {
    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    val animationScope = rememberCoroutineScope()
    val interactiveHighlight =
        remember(animationScope) {
            InteractiveHighlight(animationScope = animationScope)
        }

    Box(
        modifier =
            modifier
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { shape },
                    effects = {
                        vibrancy()
                        blur(4f.dp.toPx())
                        lens(cornerRadius.toPx().coerceAtMost(20f.dp.toPx()), 24f.dp.toPx())
                    },
                    highlight = { Highlight.Default },
                    shadow = { Shadow(alpha = 0.18f) },
                    innerShadow = { InnerShadow(radius = 6f.dp, alpha = 0.08f) },
                    layerBlock =
                        if (interactive) {
                            {
                                val width = size.width
                                val height = size.height
                                val progress = interactiveHighlight.pressProgress
                                val scale = lerp(1f, 1f + 4f.dp.toPx() / size.height, progress)
                                val maxOffset = size.minDimension
                                val initialDerivative = 0.05f
                                val offset = interactiveHighlight.offset
                                translationX = maxOffset * tanh(initialDerivative * offset.x / maxOffset)
                                translationY = maxOffset * tanh(initialDerivative * offset.y / maxOffset)
                                val maxDragScale = 4f.dp.toPx() / size.height
                                val offsetAngle = atan2(offset.y, offset.x)
                                scaleX = scale + maxDragScale *
                                    abs(cos(offsetAngle) * offset.x / size.maxDimension)
                                scaleY = scale + maxDragScale *
                                    abs(sin(offsetAngle) * offset.y / size.maxDimension)
                            }
                        } else {
                            null
                        },
                    onDrawSurface = {
                        if (tint.isSpecified) {
                            drawRect(tint, blendMode = BlendMode.Hue)
                            drawRect(tint.copy(alpha = 0.55f))
                        } else {
                            drawRect(style.containerColor)
                        }
                    },
                ).then(
                    if (interactive) {
                        Modifier
                            .then(interactiveHighlight.modifier)
                            .then(interactiveHighlight.gestureModifier)
                    } else {
                        Modifier
                    },
                ),
        content = content,
    )
}

@Composable
internal actual fun PlatformLiquidGlassContainer(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    // Capture whatever is drawn behind the container into a single shared layer, then expose it to
    // nested glass children via LocalLiquidLayerBackdrop so they all sample the same region.
    val sharedLayer: LayerBackdrop = rememberLayerBackdrop()

    Box(
        modifier = modifier.layerBackdrop(sharedLayer),
    ) {
        CompositionLocalProvider(LocalLiquidLayerBackdrop provides sharedLayer) {
            content()
        }
    }
}
