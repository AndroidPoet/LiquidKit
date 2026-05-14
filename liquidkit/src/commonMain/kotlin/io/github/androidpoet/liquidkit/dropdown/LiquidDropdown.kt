package io.github.androidpoet.liquidkit.dropdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.androidpoet.dropdown.DropDownMenuBuilder
import io.androidpoet.dropdown.Dropdown
import io.androidpoet.dropdown.Easing
import io.androidpoet.dropdown.EnterAnimation
import io.androidpoet.dropdown.ExitAnimation
import io.androidpoet.dropdown.dropDownMenu
import io.androidpoet.dropdown.dropDownMenuColors
import io.github.androidpoet.liquidkit.LiquidGlassStyle

@Composable
public fun <T : Any> LiquidDropdown(
    isOpen: Boolean,
    items: List<LiquidDropdownItem<T>>,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    style: LiquidGlassStyle = LiquidGlassStyle.NavigationBar,
    offset: DpOffset = DpOffset.Zero,
    width: Dp = 280.dp,
) {
    require(items.isNotEmpty()) { "LiquidDropdown requires at least one item." }

    Dropdown(
        modifier = modifier,
        isOpen = isOpen,
        menu = dropDownMenu<T> {
            items.forEach { item -> addLiquidItem(item) }
        },
        colors = dropDownMenuColors(
            backgroundColor = style.containerColor,
            contentColor = style.contentColor,
        ),
        offset = offset,
        enter = EnterAnimation.ElevationScale,
        exit = ExitAnimation.ElevationScale,
        easing = Easing.FastOutSlowInEasing,
        enterDuration = 220,
        exitDuration = 160,
        width = width,
        onItemSelected = { selectedKey ->
            selectedKey?.let(onItemSelected)
        },
        onDismiss = onDismiss,
    )
}

private fun <T : Any> DropDownMenuBuilder<T>.addLiquidItem(item: LiquidDropdownItem<T>) {
    item(
        id = item.key,
        title = item.title,
    ) {
        item.icon?.let { icon(it.imageVector) }
        item.children.forEach { child -> addLiquidItem(child) }
    }
}
