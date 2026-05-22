package io.github.androidpoet.liquidkit.toggle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Composable
public fun LiquidToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LiquidGlassStyle.Control,
) {
    PlatformLiquidToggle(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
