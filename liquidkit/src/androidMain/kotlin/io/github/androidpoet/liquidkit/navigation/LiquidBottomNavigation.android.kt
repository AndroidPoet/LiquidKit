package io.github.androidpoet.liquidkit.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidBottomTab
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidBottomTabs
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Composable
internal actual fun <T : Any> PlatformLiquidBottomNavigation(
    items: List<LiquidNavigationItem<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    style: LiquidGlassStyle,
) {
    val backdrop = rememberCanvasBackdrop {
        drawRect(style.containerColor)
    }
    val selectedIndex = items.indexOfFirst { it.key == selectedKey }.coerceAtLeast(0)

    LiquidBottomTabs(
        selectedTabIndex = { selectedIndex },
        onTabSelected = { index ->
            items.getOrNull(index)?.key?.let(onSelected)
        },
        backdrop = backdrop,
        tabsCount = items.size,
        modifier = modifier
            .fillMaxWidth()
        .height(64.dp),
    ) {
        items.forEach { item ->
            LiquidBottomTab(onClick = { onSelected(item.key) }) {
                LiquidBottomNavigationItem(
                    item = item,
                    selected = item.key == selectedKey,
                    style = style,
                )
            }
        }
    }
}

@Composable
private fun <T : Any> LiquidBottomNavigationItem(
    item: LiquidNavigationItem<T>,
    selected: Boolean,
    style: LiquidGlassStyle,
) {
    val contentColor = if (selected) style.selectedContentColor else style.contentColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
    ) {
        item.icon?.let { icon ->
            Image(
                painter = rememberVectorPainter(icon.vectorFor(selected)),
                contentDescription = icon.contentDescription,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(contentColor),
            )
        }

        BasicText(
            text = item.label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            ),
        )
    }
}
