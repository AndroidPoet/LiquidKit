package io.github.androidpoet.liquidkit.navigation

import androidx.compose.runtime.Immutable

@Immutable
public data class LiquidNavigationItem<T : Any>(
    public val key: T,
    public val label: String,
    public val contentDescription: String = label,
)
