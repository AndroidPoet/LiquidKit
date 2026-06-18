package io.github.androidpoet.liquidkit.dropdown

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
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
import platform.UIKit.UIAction
import platform.UIKit.UIButton
import platform.UIKit.UIButtonConfiguration
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIImage
import platform.UIKit.UIMenu
import platform.UIKit.UIMenuElement
import platform.UIKit.UIMenuElementState
import platform.UIKit.UIMenuElementAttributesDisabled

/**
 * Genuine native iOS dropdown: a [UIButton] with `showsMenuAsPrimaryAction = true`
 * presenting a [UIMenu]. On iOS 26 this picks up authentic system Liquid Glass.
 */
@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun <T : Any> PlatformLiquidDropdownMenu(
    items: List<LiquidMenuItem<T>>,
    onSelect: (T) -> Unit,
    modifier: Modifier,
    label: String,
    selectedKey: T?,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnSelect = rememberUpdatedState(onSelect)
    val currentItems = rememberUpdatedState(items)
    val currentSelected = rememberUpdatedState(selectedKey)

    UIKitView(
        factory = {
            UIButton().apply {
                showsMenuAsPrimaryAction = true
                configuration = UIButtonConfiguration.tintedButtonConfiguration()
            }
        },
        modifier = modifier.height(44.dp).widthIn(min = 120.dp),
        update = { button ->
            button.setTitle(label, forState = UIControlStateNormal)
            button.setTitleColor(style.selectedContentColor.toUIColor(), forState = UIControlStateNormal)
            button.tintColor = style.selectedContentColor.toUIColor()
            button.enabled = enabled
            button.alpha = if (enabled) 1.0 else 0.42
            button.setOpaque(false)
            button.menu = buildMenu(
                items = currentItems.value,
                selectedKey = currentSelected.value,
                onSelect = { currentOnSelect.value(it) },
            )
        },
        onRelease = {},
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.Cooperative(),
            isNativeAccessibilityEnabled = true,
            placedAsOverlay = true,
        ),
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun <T : Any> buildMenu(
    items: List<LiquidMenuItem<T>>,
    selectedKey: T?,
    onSelect: (T) -> Unit,
): UIMenu {
    val actions: List<UIMenuElement> = items.map { item ->
        val image: UIImage? = item.icon?.iosSystemNameFor(selectedKey != null && item.key == selectedKey)
            ?.let { UIImage.systemImageNamed(it) }
        UIAction.actionWithTitle(
            title = item.label,
            image = image,
            identifier = null,
            handler = { onSelect(item.key) },
        ).apply {
            state = if (selectedKey != null && item.key == selectedKey) {
                UIMenuElementState.UIMenuElementStateOn
            } else {
                UIMenuElementState.UIMenuElementStateOff
            }
            if (!item.enabled) {
                attributes = UIMenuElementAttributesDisabled
            }
        }
    }
    return UIMenu.menuWithChildren(actions)
}
