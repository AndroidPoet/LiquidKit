package io.github.androidpoet.liquidkit.picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle

/**
 * A single value option in a [LiquidPicker].
 *
 * @param key Stable identity returned through `onSelected`.
 * @param label Text shown for the option.
 */
public data class LiquidPickerOption<T : Any>(
    public val key: T,
    public val label: String,
)

/**
 * A glass value picker for selecting one option from a list.
 *
 * On Android it renders a Liquid Glass wheel/list selector. On iOS it uses a genuine
 * native `UIPickerView` via Compose `UIKitView` interop so it gets authentic system
 * Liquid Glass on iOS 26.
 *
 * @param options The selectable options.
 * @param selectedKey The currently selected key.
 * @param onSelected Invoked with the [LiquidPickerOption.key] of the chosen option.
 * @param modifier Modifier applied to the picker.
 * @param enabled Whether the picker is interactive.
 * @param style Glass style tokens.
 */
@Composable
public fun <T : Any> LiquidPicker(
    options: List<LiquidPickerOption<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
) {
    require(options.isNotEmpty()) { "LiquidPicker requires at least one option." }

    PlatformLiquidPicker(
        options = options,
        selectedKey = selectedKey,
        onSelected = onSelected,
        modifier = modifier,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun <T : Any> PlatformLiquidPicker(
    options: List<LiquidPickerOption<T>>,
    selectedKey: T,
    onSelected: (T) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
