package io.github.androidpoet.liquidkit.field

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
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UIReturnKeyType
import platform.UIKit.UISearchTextField
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldDelegateProtocol
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidSearchFieldTarget(
    var onQueryChange: (String) -> Unit,
    var onSearch: (String) -> Unit,
) : NSObject(),
    UITextFieldDelegateProtocol {
    @ObjCAction
    fun editingChanged(sender: UITextField) {
        onQueryChange(sender.text ?: "")
    }

    // Keyboard "Search" / return button submits the query.
    override fun textFieldShouldReturn(textField: UITextField): Boolean {
        onSearch(textField.text ?: "")
        textField.resignFirstResponder()
        return true
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String,
    onSearch: (String) -> Unit,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnQueryChange = rememberUpdatedState(onQueryChange)
    val currentOnSearch = rememberUpdatedState(onSearch)
    val target =
        remember {
            LiquidSearchFieldTarget(
                onQueryChange = { currentOnQueryChange.value(it) },
                onSearch = { currentOnSearch.value(it) },
            )
        }
    target.onQueryChange = { currentOnQueryChange.value(it) }
    target.onSearch = { currentOnSearch.value(it) }

    UIKitView(
        factory = {
            // UISearchTextField is the genuine iOS search-field control: it brings the system
            // magnifier glyph, clear button and (on iOS 26) authentic Liquid Glass styling.
            UISearchTextField().apply {
                text = query
                this.placeholder = placeholder
                backgroundColor = UIColor.clearColor
                setOpaque(false)
                textColor = style.selectedContentColor.toUIColor()
                this.enabled = enabled
                returnKeyType = UIReturnKeyType.UIReturnKeySearch
                delegate = target
                addTarget(
                    target = target,
                    action = NSSelectorFromString("editingChanged:"),
                    forControlEvents = UIControlEventEditingChanged,
                )
            }
        },
        modifier = modifier.height(44.dp),
        update = { field ->
            if (field.text != query) field.text = query
            field.placeholder = placeholder
            field.enabled = enabled
            field.textColor = style.selectedContentColor.toUIColor()
        },
        onRelease = { it.resignFirstResponder() },
        properties =
            UIKitInteropProperties(
                interactionMode = UIKitInteropInteractionMode.Cooperative(),
                isNativeAccessibilityEnabled = true,
                placedAsOverlay = true,
            ),
    )
}
