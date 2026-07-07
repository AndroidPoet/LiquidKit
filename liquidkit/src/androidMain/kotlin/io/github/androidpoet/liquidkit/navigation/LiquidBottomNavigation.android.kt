@file:OptIn(io.github.androidpoet.liquidkit.internal.InternalLiquidKitApi::class)

package io.github.androidpoet.liquidkit.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.LocalLiquidLayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.Backdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.LayerBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.backdrop.backdrops.rememberCanvasBackdrop
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidBottomTab
import io.github.androidpoet.liquidkit.internal.androidglass.catalog.components.LiquidBottomTabs

@Composable
internal actual fun <T : Any> PlatformLiquidBottomNavigation(
    items: List<LiquidNavigationItem<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    style: LiquidGlassStyle,
) {
    val layerCapture = LocalLiquidLayerBackdrop.current
    val canvasBackdrop = rememberCanvasBackdrop { drawRect(style.containerColor) }
    val backdrop: Backdrop = if (layerCapture is LayerBackdrop) layerCapture else canvasBackdrop
    val selectedIndex = items.indexOfFirst { it.key == selectedKey }.coerceAtLeast(0)

    LiquidBottomTabs(
        selectedTabIndex = { selectedIndex },
        onTabSelected = { index ->
            items.getOrNull(index)?.key?.let(onSelected)
        },
        backdrop = backdrop,
        tabsCount = items.size,
        modifier =
            modifier
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
        item.icon?.vectorFor(selected)?.let { vector ->
            Box {
                Image(
                    painter = rememberVectorPainter(vector),
                    contentDescription = item.icon.contentDescription,
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(contentColor),
                )
                item.badge?.let { badge ->
                    BadgeIndicator(
                        badge = badge,
                        modifier = Modifier.align(Alignment.TopEnd).offset(x = 4.dp, y = (-2).dp),
                    )
                }
            }
        }

        BasicText(
            text = item.label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style =
                TextStyle(
                    color = contentColor,
                    fontSize = 12.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                ),
        )
    }
}

@Composable
private fun BadgeIndicator(badge: Int, modifier: Modifier = Modifier) {
    if (badge == 0) {
        Box(
            modifier =
                modifier
                    .size(8.dp)
                    .background(Color(0xFFFF3B30), CircleShape),
        )
    } else {
        val count = badge.coerceAtMost(99)
        Box(
            modifier =
                modifier
                    .background(Color(0xFFFF3B30), CircleShape)
                    .padding(horizontal = 3.dp, vertical = 1.dp),
            contentAlignment = Alignment.Center,
        ) {
            BasicText(
                text = count.toString(),
                style =
                    TextStyle(
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                    ),
            )
        }
    }
}
