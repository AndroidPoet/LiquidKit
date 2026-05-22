package io.github.androidpoet.liquidkit.segmented

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle

public data class LiquidSegment<T : Any>(
    public val key: T,
    public val label: String,
)

@Composable
public fun <T : Any> LiquidSegmentedControl(
    segments: List<LiquidSegment<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LiquidGlassStyle.Control,
) {
    require(segments.isNotEmpty()) { "LiquidSegmentedControl requires at least one segment." }

    PlatformLiquidSegmentedControl(
        segments = segments,
        selectedKey = selectedKey,
        onSelected = onSelected,
        modifier = modifier,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun <T : Any> PlatformLiquidSegmentedControl(
    segments: List<LiquidSegment<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
