package io.github.androidpoet.liquidkit.surface

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Composable
public fun LiquidSurface(
    modifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.Surface,
    content: @Composable BoxScope.() -> Unit,
) {
    PlatformLiquidSurface(
        modifier = modifier,
        style = style,
        content = content,
    )
}

@Composable
internal expect fun PlatformLiquidSurface(
    modifier: Modifier,
    style: LiquidGlassStyle,
    content: @Composable BoxScope.() -> Unit,
)
