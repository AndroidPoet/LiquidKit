package io.github.androidpoet.liquidkit.icon

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
public data class LiquidIcon(
    /** Compose vector used on Android and desktop. Null if this icon is iOS-only. */
    public val imageVector: ImageVector? = null,
    public val selectedImageVector: ImageVector? = imageVector,
    public val contentDescription: String? = null,
    /** SF Symbol name used on iOS. Null if this icon is Android-only. */
    public val iosSystemName: String? = null,
    public val selectedIosSystemName: String? = iosSystemName,
) {
    public fun vectorFor(selected: Boolean): ImageVector? =
        if (selected) selectedImageVector else imageVector

    public fun iosSystemNameFor(selected: Boolean): String? =
        if (selected) selectedIosSystemName ?: iosSystemName else iosSystemName
}
