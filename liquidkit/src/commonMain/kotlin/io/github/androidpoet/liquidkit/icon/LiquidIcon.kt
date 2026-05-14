package io.github.androidpoet.liquidkit.icon

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
public data class LiquidIcon(
    public val imageVector: ImageVector,
    public val selectedImageVector: ImageVector = imageVector,
    public val contentDescription: String? = null,
) {
    public fun vectorFor(selected: Boolean): ImageVector =
        if (selected) selectedImageVector else imageVector
}
