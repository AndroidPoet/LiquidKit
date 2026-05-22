package io.github.androidpoet.liquidkit.segmented

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidBottomTab
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidBottomTabs

@Composable
internal actual fun <T : Any> PlatformLiquidSegmentedControl(
    segments: List<LiquidSegment<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val backdrop = rememberCanvasBackdrop {
        drawRect(style.containerColor)
    }
    val selectedIndex = segments.indexOfFirst { it.key == selectedKey }.coerceAtLeast(0)

    LiquidBottomTabs(
        selectedTabIndex = { selectedIndex },
        onTabSelected = { index ->
            if (enabled) segments.getOrNull(index)?.key?.let(onSelected)
        },
        backdrop = backdrop,
        tabsCount = segments.size,
        modifier = modifier.alpha(if (enabled) 1f else 0.42f),
    ) {
        segments.forEach { segment ->
            val selected = segment.key == selectedKey
            LiquidBottomTab(
                onClick = {
                    if (enabled) onSelected(segment.key)
                },
            ) {
                BasicText(
                    text = segment.label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = if (selected) style.selectedContentColor else style.contentColor,
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    ),
                )
            }
        }
    }
}
