package io.github.androidpoet.liquidkit.field

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle
import io.github.androidpoet.liquidkit.icon.LiquidIcon

/**
 * A Liquid Glass text input.
 *
 * On Android the field renders over the relocated Liquid Glass backdrop engine wrapping a
 * Compose `BasicTextField`. On iOS it hosts a genuine native `UITextField` via Compose
 * `UIKitView` interop so it picks up authentic system Liquid Glass on iOS 26.
 *
 * @param value current text value
 * @param onValueChange invoked with the new text as the user types
 * @param placeholder hint shown while [value] is empty
 * @param leadingIcon optional icon rendered before the text
 * @param trailingIcon optional icon rendered after the text
 * @param singleLine whether the field is constrained to a single line
 * @param enabled whether the field accepts input
 * @param style the Liquid Glass style applied to the container
 */
@Composable
public fun LiquidTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: LiquidIcon? = null,
    trailingIcon: LiquidIcon? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
) {
    PlatformLiquidTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String,
    leadingIcon: LiquidIcon?,
    trailingIcon: LiquidIcon?,
    singleLine: Boolean,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
