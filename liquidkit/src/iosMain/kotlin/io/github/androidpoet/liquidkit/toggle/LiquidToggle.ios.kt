package io.github.androidpoet.liquidkit.toggle

import androidx.compose.foundation.layout.requiredSize
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
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISwitch
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidSwitchTarget(
    private val onCheckedChange: (Boolean) -> Unit,
) : NSObject() {
    @ObjCAction
    fun valueChanged(sender: UISwitch) {
        onCheckedChange(sender.isOn())
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun PlatformLiquidToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnCheckedChange = rememberUpdatedState(onCheckedChange)
    val target = remember {
        LiquidSwitchTarget { currentOnCheckedChange.value(it) }
    }

    UIKitView(
        factory = {
            UISwitch().apply {
                setOn(checked)
                this.enabled = enabled
                onTintColor = style.selectedContentColor.toUIColor()
                backgroundColor = UIColor.clearColor
                setOpaque(false)
                addTarget(
                    target = target,
                    action = NSSelectorFromString("valueChanged:"),
                    forControlEvents = UIControlEventValueChanged,
                )
            }
        },
        modifier = modifier.requiredSize(width = 70.dp, height = 31.dp),
        update = { uiSwitch ->
            if (uiSwitch.isOn() != checked) {
                uiSwitch.setOn(checked, animated = true)
            }
            uiSwitch.enabled = enabled
            uiSwitch.onTintColor = style.selectedContentColor.toUIColor()
        },
        onRelease = {},
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.Cooperative(),
            isNativeAccessibilityEnabled = true,
            placedAsOverlay = true,
        ),
    )
}
