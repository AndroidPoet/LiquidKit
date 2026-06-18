@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.shapes.Capsule
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCombinedBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.layerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.drawBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.blur
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.lens
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.vibrancy
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.highlight.Highlight
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.shadow.Shadow

private val RowHeight = 44.dp
private val WheelHeight = RowHeight * 5

@Composable
internal actual fun <T : Any> PlatformLiquidPicker(
    options: List<LiquidPickerOption<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val isLight = !isSystemInDarkTheme()
    val containerColor = if (isLight) Color.White.copy(0.42f) else Color.Black.copy(0.40f)

    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val outerBackdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    val trackLayer = rememberLayerBackdrop()
    val listState = rememberLazyListState()
    val selectedIndex = options.indexOfFirst { it.key == selectedKey }.coerceAtLeast(0)

    LaunchedEffect(selectedIndex) {
        // Center the selected row within the 5-row wheel (2 rows above).
        listState.animateScrollToItem(selectedIndex.coerceAtLeast(0))
    }

    Box(
        modifier
            .alpha(if (enabled) 1f else 0.42f)
            .fillMaxWidth()
            .height(WheelHeight),
        contentAlignment = Alignment.Center,
    ) {
        // Glass container that the center selection refracts over.
        LazyColumn(
            modifier = Modifier
                .layerBackdrop(trackLayer)
                .drawBackdrop(
                    backdrop = outerBackdrop,
                    shape = { com.kyant.shapes.RoundedRectangle(20.dp) },
                    effects = {
                        vibrancy()
                        blur(8f.dp.toPx())
                        lens(8f.dp.toPx(), 16f.dp.toPx())
                    },
                    onDrawSurface = { drawRect(containerColor) },
                )
                .fillMaxWidth()
                .height(WheelHeight),
            state = listState,
            contentPadding = PaddingValues(vertical = RowHeight * 2),
        ) {
            items(options, key = { it.key }) { option ->
                val isSelected = option.key == selectedKey
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(RowHeight)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            enabled = enabled,
                            role = Role.Button,
                            onClick = { onSelected(option.key) },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = option.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = if (isSelected) style.selectedContentColor else style.contentColor,
                            fontSize = if (isSelected) 19.sp else 17.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }

        // Floating glass selection band over the center row.
        Box(
            Modifier
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(outerBackdrop, trackLayer),
                    shape = { Capsule() },
                    effects = {
                        lens(6f.dp.toPx(), 12f.dp.toPx(), chromaticAberration = true)
                    },
                    highlight = { Highlight.Default },
                    shadow = { Shadow(alpha = 0.12f) },
                    onDrawSurface = {
                        val surfaceColor = if (isLight) Color.Black.copy(0.05f) else Color.White.copy(0.08f)
                        drawRect(surfaceColor)
                    },
                )
                .fillMaxWidth()
                .height(RowHeight),
        )
    }
}
