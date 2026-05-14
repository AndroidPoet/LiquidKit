package io.github.androidpoet.liquidkit.navigation

import androidx.compose.runtime.Immutable
import io.github.androidpoet.liquidkit.icon.LiquidIcon

@Immutable
public data class LiquidNavigationItem<T : Any>(
    public val key: T,
    public val label: String,
    public val icon: LiquidIcon? = null,
    public val contentDescription: String = label,
)
