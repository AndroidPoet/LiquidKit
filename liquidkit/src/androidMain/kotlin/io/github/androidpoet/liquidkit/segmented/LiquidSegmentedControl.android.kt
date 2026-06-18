@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.segmented

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCombinedBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.layerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.drawBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.blur
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.lens
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.vibrancy
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.highlight.Highlight
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.shadow.InnerShadow
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.shadow.Shadow
import com.kyant.shapes.Capsule

@Composable
internal actual fun <T : Any> PlatformLiquidSegmentedControl(
    segments: List<LiquidSegment<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val isLight = !isSystemInDarkTheme()
    val containerColor = if (isLight) Color(0xFFFAFAFA).copy(0.4f) else Color(0xFF121212).copy(0.4f)

    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val outerBackdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    val segCount = segments.size
    val selectedIndex = segments.indexOfFirst { it.key == selectedKey }.coerceAtLeast(0)

    // Track layer: the container pill captures itself for the indicator to refract over
    val trackLayer = rememberLayerBackdrop()

    BoxWithConstraints(
        modifier
            .alpha(if (enabled) 1f else 0.42f)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart,
    ) {
        val segWidth = constraints.maxWidth.toFloat() / segCount

        // Animate indicator position; LaunchedEffect re-runs on every selectedIndex change
        val indicatorOffset = remember { Animatable(selectedIndex * segWidth) }
        LaunchedEffect(selectedIndex, segWidth) {
            indicatorOffset.animateTo(selectedIndex * segWidth, spring(0.8f, 600f))
        }

        // Container row with glass backdrop
        Row(
            Modifier
                .layerBackdrop(trackLayer)
                .drawBackdrop(
                    backdrop = outerBackdrop,
                    shape = { Capsule() },
                    effects = {
                        vibrancy()
                        blur(8f.dp.toPx())
                        lens(8f.dp.toPx(), 16f.dp.toPx())
                    },
                    onDrawSurface = { drawRect(containerColor) },
                )
                .height(40.dp)
                .fillMaxWidth()
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            segments.forEach { segment ->
                val isSelected = segment.key == selectedKey
                Box(
                    Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            enabled = enabled,
                            role = Role.Button,
                            onClick = { onSelected(segment.key) },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    val contentColor = if (isSelected) style.selectedContentColor else style.contentColor
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                    ) {
                        segment.icon?.vectorFor(isSelected)?.let { vector ->
                            Image(
                                painter = rememberVectorPainter(vector),
                                contentDescription = segment.icon.contentDescription,
                                modifier = Modifier.size(14.dp),
                                colorFilter = ColorFilter.tint(contentColor),
                            )
                        }
                        BasicText(
                            text = segment.label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = contentColor,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            ),
                        )
                    }
                }
            }
        }

        // Floating glass indicator (like LiquidBottomTabs selected tab)
        Box(
            Modifier
                .graphicsLayer { translationX = indicatorOffset.value }
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(outerBackdrop, trackLayer),
                    shape = { Capsule() },
                    effects = {
                        lens(6f.dp.toPx(), 12f.dp.toPx(), chromaticAberration = true)
                    },
                    highlight = { Highlight.Default },
                    shadow = { Shadow(alpha = 0.15f) },
                    innerShadow = { InnerShadow(radius = 4f.dp, alpha = 0.08f) },
                    onDrawSurface = {
                        val surfaceColor = if (isLight) Color.Black.copy(0.06f) else Color.White.copy(0.06f)
                        drawRect(surfaceColor)
                    },
                )
                .height(34.dp)
                .fillMaxWidth(1f / segCount),
        )
    }
}
