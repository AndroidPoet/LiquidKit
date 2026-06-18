package io.github.androidpoet.liquidkit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class LiquidGlassStyleTest {
    @Test
    fun test_liquidGlassStyle_noOverrides_returnsControlStyle() {
        val style = liquidGlassStyle()

        assertEquals(LiquidGlassStyle.Control, style)
    }

    @Test
    fun test_liquidGlassStyle_baseNavigationBar_preservesBaseValues() {
        val style = liquidGlassStyle(base = LiquidGlassStyle.NavigationBar)

        assertEquals(LiquidGlassStyle.NavigationBar, style)
    }

    @Test
    fun test_liquidGlassStyle_overridesOnlyProvidedValues() {
        val style =
            liquidGlassStyle(
                base = LiquidGlassStyle.NavigationBar,
                cornerRadius = 12.dp,
                containerColor = Color.Red,
            )

        assertEquals(12.dp, style.cornerRadius)
        assertEquals(Color.Red, style.containerColor)
        assertEquals(LiquidGlassStyle.NavigationBar.selectedContentColor, style.selectedContentColor)
        assertEquals(LiquidGlassStyle.NavigationBar.contentColor, style.contentColor)
    }
}
