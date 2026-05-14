package io.github.androidpoet.liquidkit.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.surface.LiquidSurface

@Composable
public fun <T : Any> LiquidBottomNavigation(
    items: List<LiquidNavigationItem<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
) {
    require(items.isNotEmpty()) { "LiquidBottomNavigation requires at least one item." }

    LiquidSurface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        style = style,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                LiquidBottomNavigationItem(
                    item = item,
                    selected = item.key == selectedKey,
                    onSelected = { onSelected(item.key) },
                    style = style,
                )
            }
        }
    }
}

@Composable
private fun <T : Any> RowScope.LiquidBottomNavigationItem(
    item: LiquidNavigationItem<T>,
    selected: Boolean,
    onSelected: () -> Unit,
    style: LiquidGlassStyle,
) {
    BasicText(
        text = item.label,
        modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .clip(RoundedCornerShape(style.cornerRadius - 4.dp))
            .background(if (selected) style.selectedContainerColor else androidx.compose.ui.graphics.Color.Transparent)
            .selectable(
                selected = selected,
                role = Role.Tab,
                onClick = onSelected,
            )
            .semantics {
                contentDescription = item.contentDescription
            }
            .padding(horizontal = 8.dp, vertical = 18.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = TextStyle(
            color = if (selected) style.selectedContentColor else style.contentColor,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        ),
    )
}
