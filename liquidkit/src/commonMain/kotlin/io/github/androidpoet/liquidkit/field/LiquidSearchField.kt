package io.github.androidpoet.liquidkit.field

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.LocalLiquidGlassStyle

/**
 * A Liquid Glass search pill, matching the iOS search-field idiom.
 *
 * On Android this renders a glass capsule (consistent with the sample's search pill) wrapping a
 * Compose `BasicTextField` with a leading magnifier and a trailing clear affordance. On iOS it
 * hosts a genuine native `UISearchTextField` via Compose `UIKitView` interop so it gets authentic
 * system Liquid Glass on iOS 26.
 *
 * @param query current search query
 * @param onQueryChange invoked with the new query as the user types
 * @param placeholder hint shown while [query] is empty
 * @param onSearch invoked with the current query when the user submits (keyboard search action)
 * @param enabled whether the field accepts input
 * @param style the Liquid Glass style applied to the pill
 */
@Composable
public fun LiquidSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    onSearch: (String) -> Unit = {},
    enabled: Boolean = true,
    style: LiquidGlassStyle = LocalLiquidGlassStyle.current,
) {
    PlatformLiquidSearchField(
        query = query,
        onQueryChange = onQueryChange,
        modifier = modifier,
        placeholder = placeholder,
        onSearch = onSearch,
        enabled = enabled,
        style = style,
    )
}

@Composable
internal expect fun PlatformLiquidSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String,
    onSearch: (String) -> Unit,
    enabled: Boolean,
    style: LiquidGlassStyle,
)
