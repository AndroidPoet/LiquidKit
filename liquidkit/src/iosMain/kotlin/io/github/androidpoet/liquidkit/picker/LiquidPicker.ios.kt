package io.github.androidpoet.liquidkit.picker

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
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import platform.UIKit.UIColor
import platform.UIKit.UIPickerView
import platform.UIKit.UIPickerViewDataSourceProtocol
import platform.UIKit.UIPickerViewDelegateProtocol
import platform.darwin.NSInteger
import platform.darwin.NSObject

private class LiquidPickerCoordinator(
    var titles: List<String>,
    var onRowSelected: (Int) -> Unit,
) : NSObject(),
    UIPickerViewDataSourceProtocol,
    UIPickerViewDelegateProtocol {
    override fun numberOfComponentsInPickerView(pickerView: UIPickerView): NSInteger = 1L

    override fun pickerView(
        pickerView: UIPickerView,
        numberOfRowsInComponent: NSInteger,
    ): NSInteger = titles.size.toLong()

    @ObjCSignatureOverride
    override fun pickerView(
        pickerView: UIPickerView,
        titleForRow: NSInteger,
        forComponent: NSInteger,
    ): String? = titles.getOrNull(titleForRow.toInt())

    @ObjCSignatureOverride
    override fun pickerView(
        pickerView: UIPickerView,
        didSelectRow: NSInteger,
        inComponent: NSInteger,
    ) {
        onRowSelected(didSelectRow.toInt())
    }
}

/**
 * Genuine native iOS picker: a [UIPickerView] driven by a Kotlin coordinator acting as
 * data source + delegate. On iOS 26 it adopts the authentic system Liquid Glass styling.
 */
@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
internal actual fun <T : Any> PlatformLiquidPicker(
    options: List<LiquidPickerOption<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
) {
    val currentOnSelected = rememberUpdatedState(onSelected)
    val currentOptions = rememberUpdatedState(options)

    val coordinator =
        remember {
            LiquidPickerCoordinator(
                titles = options.map { it.label },
                onRowSelected = { index ->
                    currentOptions.value
                        .getOrNull(index)
                        ?.key
                        ?.let(currentOnSelected.value)
                },
            )
        }
    coordinator.titles = options.map { it.label }
    coordinator.onRowSelected = { index ->
        currentOptions.value
            .getOrNull(index)
            ?.key
            ?.let(currentOnSelected.value)
    }

    val selectedIndex = options.indexOfFirst { it.key == selectedKey }.coerceAtLeast(0)

    UIKitView(
        factory = {
            UIPickerView().apply {
                dataSource = coordinator
                delegate = coordinator
                backgroundColor = UIColor.clearColor
                setOpaque(false)
            }
        },
        modifier = modifier.fillMaxWidth().height(216.dp),
        update = { picker ->
            picker.userInteractionEnabled = enabled
            picker.alpha = if (enabled) 1.0 else 0.42
            picker.reloadAllComponents()
            if (picker.selectedRowInComponent(0) != selectedIndex.toLong()) {
                picker.selectRow(selectedIndex.toLong(), inComponent = 0, animated = false)
            }
        },
        onRelease = {},
        properties =
            UIKitInteropProperties(
                interactionMode = UIKitInteropInteractionMode.Cooperative(),
                isNativeAccessibilityEnabled = true,
                placedAsOverlay = true,
            ),
    )
}
