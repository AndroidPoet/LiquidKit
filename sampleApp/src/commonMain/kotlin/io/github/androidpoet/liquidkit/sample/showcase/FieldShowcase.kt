package io.github.androidpoet.liquidkit.sample.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.LiquidGlassStyle
import io.github.androidpoet.liquidkit.field.LiquidSearchField
import io.github.androidpoet.liquidkit.field.LiquidTextField

/**
 * Self-contained showcase for [LiquidTextField] and [LiquidSearchField].
 *
 * Drop this composable anywhere (it manages its own state and background) to preview the
 * Liquid Glass field components on Android and iOS.
 */
@Composable
public fun FieldShowcase(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var lastSearch by remember { mutableStateOf("") }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1B3A6B), Color(0xFF6B3A8C), Color(0xFFB0507A)),
                    ),
                ).verticalScroll(rememberScrollState())
                .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionTitle("Liquid Glass Fields")

        SectionLabel("Text field")
        LiquidTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "Full name",
            style = LiquidGlassStyle.SurfaceDark,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("Text field (disabled)")
        LiquidTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "you@example.com",
            enabled = false,
            style = LiquidGlassStyle.SurfaceDark,
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("Search field")
        LiquidSearchField(
            query = query,
            onQueryChange = { query = it },
            onSearch = { lastSearch = it },
            placeholder = "Search components",
            style = LiquidGlassStyle.ControlDark,
            modifier = Modifier.fillMaxWidth(),
        )

        if (lastSearch.isNotEmpty()) {
            BasicText(
                text = "Searched: \"$lastSearch\"",
                style = TextStyle(color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp),
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    BasicText(
        text = text,
        style = TextStyle(color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold),
    )
}

@Composable
private fun SectionLabel(text: String) {
    BasicText(
        text = text,
        style =
            TextStyle(
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            ),
    )
}
