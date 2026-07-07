@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.icon.LiquidIcon
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidButton as EngineLiquidButton

@Composable
internal actual fun PlatformLiquidFab(
    icon: LiquidIcon,
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
    val iconTint = if (prominent) style.selectedContentColor else style.contentColor

    EngineLiquidButton(
        onClick = { if (enabled) onClick() },
        backdrop = backdrop,
        isInteractive = enabled,
        surfaceColor = if (prominent) style.selectedContainerColor else Color.Unspecified,
        modifier = modifier.size(56.dp).alpha(if (enabled) 1f else 0.42f),
    ) {
        icon.vectorFor(true)?.let { vector ->
            Image(
                painter = rememberVectorPainter(vector),
                contentDescription = icon.contentDescription,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(iconTint),
            )
        }
    }
}
