package io.github.androidpoet.liquidkit.toggle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidToggle
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Composable
internal actual fun PlatformLiquidToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val backdrop = rememberCanvasBackdrop {
        drawRect(style.containerColor)
    }

    LiquidToggle(
        selected = { checked },
        onSelect = { if (enabled) onCheckedChange(it) },
        backdrop = backdrop,
        modifier = modifier.alpha(if (enabled) 1f else 0.42f),
    )
}
