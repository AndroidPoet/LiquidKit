package io.github.androidpoet.liquidkit.dropdown

import androidx.compose.runtime.Immutable

@Immutable
public data class LiquidDropdownItem<T : Any>(
    public val key: T,
    public val title: String,
    public val children: List<LiquidDropdownItem<T>> = emptyList(),
)
