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
import io.github.androidpoet.liquidkit.icon.LiquidIcon
import io.github.androidpoet.liquidkit.internal.toUIColor
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UITextBorderStyle
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldViewMode
import platform.UIKit.UIView
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidTextFieldTarget(
    var onValueChange: (String) -> Unit,
) : NSObject() {
    @ObjCAction
    fun editingChanged(sender: UITextField) {
        onValueChange(sender.text ?: "")
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String,
    leadingIcon: LiquidIcon?,
    trailingIcon: LiquidIcon?,
    singleLine: Boolean,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnValueChange = rememberUpdatedState(onValueChange)
    val target = remember { LiquidTextFieldTarget { currentOnValueChange.value(it) } }
    target.onValueChange = { currentOnValueChange.value(it) }

    UIKitView(
        factory = {
            UITextField().apply {
                text = value
                this.placeholder = placeholder
                // Borderless: the native field renders on top of the LiquidKit glass surface;
                // on iOS 26 a borderless UITextField inherits the system glass treatment.
                borderStyle = UITextBorderStyle.UITextBorderStyleNone
                backgroundColor = UIColor.clearColor
                setOpaque(false)
                textColor = style.selectedContentColor.toUIColor()
                this.enabled = enabled
                leadingIcon?.sfImageView()?.let {
                    leftView = it
                    leftViewMode = UITextFieldViewMode.UITextFieldViewModeAlways
                }
                trailingIcon?.sfImageView()?.let {
                    rightView = it
                    rightViewMode = UITextFieldViewMode.UITextFieldViewModeAlways
                }
                addTarget(
                    target = target,
                    action = NSSelectorFromString("editingChanged:"),
                    forControlEvents = UIControlEventEditingChanged,
                )
            }
        },
        modifier = modifier.height(52.dp),
        update = { field ->
            if (field.text != value) field.text = value
            field.placeholder = placeholder
            field.enabled = enabled
            field.textColor = style.selectedContentColor.toUIColor()
        },
        onRelease = { it.resignFirstResponder() },
        properties =
            UIKitInteropProperties(
                // Cooperative + overlay lets the native field receive taps and become first responder,
                // so the system keyboard shows and typing works without manual focus wiring.
                interactionMode = UIKitInteropInteractionMode.Cooperative(),
                isNativeAccessibilityEnabled = true,
                placedAsOverlay = true,
            ),
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun LiquidIcon.sfImageView(): UIView? {
    val name = iosSystemNameFor(selected = false) ?: return null
    val image = UIImage.systemImageNamed(name) ?: return null
    return UIImageView(image = image)
}
