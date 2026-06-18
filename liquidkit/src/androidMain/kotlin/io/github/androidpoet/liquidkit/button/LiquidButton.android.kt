@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.button

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidButton as EngineLiquidButton

@Composable
internal actual fun PlatformLiquidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    variant: LiquidButtonVariant,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop

    val prominent = variant == LiquidButtonVariant.GlassProminent
    val labelColor = if (prominent) style.selectedContentColor else style.contentColor

    EngineLiquidButton(
        onClick = { if (enabled) onClick() },
        backdrop = backdrop,
        isInteractive = enabled,
        surfaceColor = if (prominent) style.selectedContainerColor else androidx.compose.ui.graphics.Color.Unspecified,
        modifier = modifier.alpha(if (enabled) 1f else 0.42f),
    ) {
        BasicText(
            text = text,
            modifier = Modifier.padding(horizontal = 4.dp),
            style =
                TextStyle(
                    color = labelColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}
