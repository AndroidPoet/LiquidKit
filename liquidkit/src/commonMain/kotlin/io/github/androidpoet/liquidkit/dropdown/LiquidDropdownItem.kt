package io.github.androidpoet.liquidkit.dropdown

import androidx.compose.runtime.Immutable
import io.github.androidpoet.liquidkit.icon.LiquidIcon

@Immutable
public data class LiquidDropdownItem<T : Any>(
    public val key: T,
    public val title: String,
    public val icon: LiquidIcon? = null,
    public val children: List<LiquidDropdownItem<T>> = emptyList(),
)
