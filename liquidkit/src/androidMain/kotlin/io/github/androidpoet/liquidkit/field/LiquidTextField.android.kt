@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.field

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.icon.LiquidIcon
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
internal actual fun PlatformLiquidTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String,
    leadingIcon: LiquidIcon?,
    trailingIcon: LiquidIcon?,
    singleLine: Boolean,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    val fieldShape = RoundedCornerShape(style.cornerRadius)

    Row(
        modifier =
            modifier
                .alpha(if (enabled) 1f else 0.42f)
                .fillMaxWidth()
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { fieldShape },
                    effects = {
                        vibrancy()
                        blur(8f.dp.toPx())
                        lens(8f.dp.toPx(), 16f.dp.toPx())
                    },
                    highlight = { Highlight.Default },
                    onDrawSurface = { drawRect(style.containerColor) },
                ).heightIn(min = 52.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        leadingIcon?.vectorFor(selected = false)?.let { vector ->
            Image(
                painter = rememberVectorPainter(vector),
                contentDescription = leadingIcon.contentDescription,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(style.contentColor),
            )
        }

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (value.isEmpty() && placeholder.isNotEmpty()) {
                androidx.compose.foundation.text.BasicText(
                    text = placeholder,
                    style = TextStyle(color = style.contentColor.copy(alpha = 0.6f), fontSize = 16.sp),
                )
            }
            BasicTextField(
                value = value,
                onValueChange = { if (enabled) onValueChange(it) },
                enabled = enabled,
                singleLine = singleLine,
                textStyle = TextStyle(color = style.selectedContentColor, fontSize = 16.sp),
                cursorBrush = SolidColor(style.selectedContentColor),
                keyboardOptions = KeyboardOptions(imeAction = if (singleLine) ImeAction.Done else ImeAction.Default),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        trailingIcon?.vectorFor(selected = false)?.let { vector ->
            Image(
                painter = rememberVectorPainter(vector),
                contentDescription = trailingIcon.contentDescription,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(style.contentColor),
            )
        }
    }
}
