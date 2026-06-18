package io.github.androidpoet.liquidkit.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.internal.toUIColor
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.UIKit.UITabBar
import platform.UIKit.UITabBarAppearance
import platform.UIKit.UITabBarDelegateProtocol
import platform.UIKit.UITabBarItem
import platform.darwin.NSObject

private class LiquidTabBarDelegate(
    private val onItemSelected: (Int) -> Unit,
) : NSObject(), UITabBarDelegateProtocol {
    override fun tabBar(tabBar: UITabBar, didSelectItem: UITabBarItem) {
        onItemSelected(didSelectItem.tag.toInt())
    }
}

@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun <T : Any> PlatformLiquidBottomNavigation(
    items: List<LiquidNavigationItem<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    style: LiquidGlassStyle,
) {
    val currentOnSelected = rememberUpdatedState(onSelected)
    val delegate = remember {
        LiquidTabBarDelegate { index ->
            items.getOrNull(index)?.key?.let(currentOnSelected.value)
        }
    }

    UIKitView(
        factory = {
            UITabBar(frame = CGRectMake(0.0, 0.0, 400.0, 83.0)).apply {
                this.delegate = delegate
                configureLiquidTabBar(items, selectedKey, style)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(83.dp),
        update = { tabBar ->
            tabBar.delegate = delegate
            tabBar.configureLiquidTabBar(items, selectedKey, style)
        },
        onRelease = {},
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.Cooperative(),
            isNativeAccessibilityEnabled = true,
            placedAsOverlay = true,
        ),
    )
}

private fun <T : Any> UITabBar.configureLiquidTabBar(
    navigationItems: List<LiquidNavigationItem<T>>,
    selectedKey: T,
    style: LiquidGlassStyle,
) {
    val tabItems = navigationItems.mapIndexed { index, item ->
        val selected = item.key == selectedKey
        val image = item.icon?.iosSystemNameFor(selected)?.let { UIImage.systemImageNamed(it) }
        UITabBarItem(
            title = item.label,
            image = image,
            selectedImage = image,
        ).apply {
            tag = index.toLong()
            badgeValue = when (item.badge) {
                null -> null
                0 -> ""
                else -> item.badge.coerceAtMost(99).toString()
            }
        }
    }

    setItems(tabItems, animated = false)
    selectedItem = tabItems.getOrNull(navigationItems.indexOfFirst { it.key == selectedKey })

    val appearance = UITabBarAppearance()
    appearance.configureWithDefaultBackground()
    standardAppearance = appearance
    scrollEdgeAppearance = appearance
    tintColor = style.selectedContentColor.toUIColor()
    unselectedItemTintColor = style.contentColor.toUIColor()
    backgroundColor = UIColor.clearColor
}
