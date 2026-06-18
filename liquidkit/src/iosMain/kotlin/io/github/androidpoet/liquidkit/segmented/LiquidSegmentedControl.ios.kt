package io.github.androidpoet.liquidkit.segmented

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
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIImage
import platform.UIKit.UIControlStateSelected
import platform.UIKit.UISegmentedControl
import platform.darwin.NSObject

@OptIn(BetaInteropApi::class)
private class LiquidSegmentedControlTarget(
    var onIndexSelected: (Int) -> Unit,
) : NSObject() {
    @ObjCAction
    fun valueChanged(sender: UISegmentedControl) {
        onIndexSelected(sender.selectedSegmentIndex.toInt())
    }
}

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun <T : Any> PlatformLiquidSegmentedControl(
    segments: List<LiquidSegment<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnSelected = rememberUpdatedState(onSelected)
    val target = remember {
        LiquidSegmentedControlTarget { index ->
            segments.getOrNull(index)?.key?.let(currentOnSelected.value)
        }
    }
    target.onIndexSelected = { index ->
        segments.getOrNull(index)?.key?.let(currentOnSelected.value)
    }

    UIKitView(
        factory = {
            UISegmentedControl().apply {
                configureLiquidSegmentedControl(segments, selectedKey, enabled, style)
                addTarget(
                    target = target,
                    action = NSSelectorFromString("valueChanged:"),
                    forControlEvents = UIControlEventValueChanged,
                )
            }
        },
        modifier = modifier.height(44.dp),
        update = { segmentedControl ->
            segmentedControl.configureLiquidSegmentedControl(segments, selectedKey, enabled, style)
        },
        onRelease = {},
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.Cooperative(),
            isNativeAccessibilityEnabled = true,
            placedAsOverlay = true,
        ),
    )
}

private fun <T : Any> UISegmentedControl.configureLiquidSegmentedControl(
    segments: List<LiquidSegment<T>>,
    selectedKey: T,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    removeAllSegments()
    segments.forEachIndexed { index, segment ->
        val systemImageName = segment.icon?.iosSystemName
        if (systemImageName != null) {
            val image = UIImage.systemImageNamed(systemImageName)
            if (image != null) {
                insertSegmentWithImage(image, atIndex = index.toULong(), animated = false)
            } else {
                insertSegmentWithTitle(segment.label, atIndex = index.toULong(), animated = false)
            }
        } else {
            insertSegmentWithTitle(segment.label, atIndex = index.toULong(), animated = false)
        }
        setEnabled(enabled, forSegmentAtIndex = index.toULong())
    }
    selectedSegmentIndex = segments.indexOfFirst { it.key == selectedKey }.coerceAtLeast(0).toLong()
    selectedSegmentTintColor = style.selectedContainerColor.toUIColor()
    backgroundColor = style.containerColor.toUIColor()
    tintColor = style.selectedContentColor.toUIColor()
    setTitleTextAttributes(
        mapOf<Any?, Any?>(
            NSForegroundColorAttributeName to style.contentColor.toUIColor(),
        ),
        forState = UIControlStateNormal,
    )
    setTitleTextAttributes(
        mapOf<Any?, Any?>(
            NSForegroundColorAttributeName to style.selectedContentColor.toUIColor(),
        ),
        forState = UIControlStateSelected,
    )
    this.enabled = enabled
    alpha = if (enabled) 1.0 else 0.42
    setOpaque(false)
}
