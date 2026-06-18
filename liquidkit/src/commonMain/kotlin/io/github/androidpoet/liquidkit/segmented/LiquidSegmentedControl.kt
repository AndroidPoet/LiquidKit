package io.github.androidpoet.liquidkit.segmented

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle
import io.github.androidpoet.liquidkit.icon.LiquidIcon

public data class LiquidSegment<T : Any>(
    public val key: T,
    public val label: String,
    /** Optional icon shown alongside the label. Platform renderers display it when supported. */
    public val icon: LiquidIcon? = null,
)

@Composable
public fun <T : Any> LiquidSegmentedControl(
    segments: List<LiquidSegment<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
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
