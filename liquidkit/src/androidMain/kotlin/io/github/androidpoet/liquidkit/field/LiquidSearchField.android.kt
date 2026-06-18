@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.shapes.Capsule
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.drawBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.blur
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.lens
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.effects.vibrancy
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.highlight.Highlight

@Composable
internal actual fun PlatformLiquidSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String,
    onSearch: (String) -> Unit,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    Row(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.42f)
            .fillMaxWidth()
            .drawBackdrop(
                backdrop = backdrop,
                shape = { Capsule() },
                effects = {
                    vibrancy()
                    blur(8f.dp.toPx())
                    lens(8f.dp.toPx(), 16f.dp.toPx())
                },
                highlight = { Highlight.Default },
                onDrawSurface = { drawRect(style.containerColor) },
            )
            .height(44.dp)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Magnifier glyph drawn with Canvas so we need no icon dependency.
        Box(
            modifier = Modifier
                .size(16.dp)
                .androidMagnifier(style.contentColor),
        )

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (query.isEmpty() && placeholder.isNotEmpty()) {
                BasicText(
                    text = placeholder,
                    style = TextStyle(color = style.contentColor.copy(alpha = 0.6f), fontSize = 15.sp),
                )
            }
            BasicTextField(
                value = query,
                onValueChange = { if (enabled) onQueryChange(it) },
                enabled = enabled,
                singleLine = true,
                textStyle = TextStyle(color = style.selectedContentColor, fontSize = 15.sp),
                cursorBrush = SolidColor(style.selectedContentColor),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (query.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        enabled = enabled,
                        role = Role.Button,
                        onClick = { onQueryChange("") },
                    )
                    .androidClearGlyph(style.contentColor),
            )
        }
    }
}

private fun Modifier.androidMagnifier(color: Color): Modifier =
    this.drawBehind {
        val dim = size.minDimension
        val r = dim * 0.32f
        val stroke = dim * 0.12f
        val center = Offset(size.width * 0.42f, size.height * 0.42f)
        drawCircle(color = color, radius = r, center = center, style = Stroke(width = stroke))
        val start = Offset(center.x + r * 0.7f, center.y + r * 0.7f)
        drawLine(
            color = color,
            start = start,
            end = Offset(size.width * 0.92f, size.height * 0.92f),
            strokeWidth = stroke,
        )
    }

private fun Modifier.androidClearGlyph(color: Color): Modifier =
    this.drawBehind {
        val dim = size.minDimension
        val r = dim * 0.5f
        drawCircle(color = color.copy(alpha = 0.30f), radius = r)
        val inset = dim * 0.32f
        val stroke = dim * 0.10f
        drawLine(
            color = color,
            start = Offset(inset, inset),
            end = Offset(size.width - inset, size.height - inset),
            strokeWidth = stroke,
        )
        drawLine(
            color = color,
            start = Offset(size.width - inset, inset),
            end = Offset(inset, size.height - inset),
            strokeWidth = stroke,
        )
    }
