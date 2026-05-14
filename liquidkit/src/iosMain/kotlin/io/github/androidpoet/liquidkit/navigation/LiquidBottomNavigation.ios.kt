package io.github.androidpoet.liquidkit.navigation

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.viewinterop.UIKitView
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIBlurEffect
import platform.UIKit.UIBlurEffectStyle
import platform.UIKit.UIVisualEffectView

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun <T : Any> LiquidBottomNavigationPlatform(
    items: List<LiquidNavigationItem<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    style: LiquidGlassStyle,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(style.cornerRadius)),
    ) {
        UIKitView(
            factory = {
                UIVisualEffectView(
                    effect = UIBlurEffect.effectWithStyle(UIBlurEffectStyle.UIBlurEffectStyleRegular),
                )
            },
            modifier = Modifier.fillMaxSize(),
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup()
                .padding(4.dp),
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
