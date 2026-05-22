package io.github.androidpoet.liquidkit.play

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Composable
public fun LiquidPlayButton(
    playing: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LiquidGlassStyle.Control,
) {
    PlatformLiquidPlayButton(
        playing = playing,
        onPlayingChange = onPlayingChange,
        modifier = modifier,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidPlayButton(
    playing: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
