package io.github.androidpoet.liquidkit.sample.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.androidpoet.liquidkit.dropdown.LiquidDropdownMenu
import io.github.androidpoet.liquidkit.dropdown.LiquidMenuItem
import io.github.androidpoet.liquidkit.picker.LiquidPicker
import io.github.androidpoet.liquidkit.picker.LiquidPickerOption

private enum class SortOrder(
    val label: String,
) {
    Newest("Newest"),
    Oldest("Oldest"),
    Name("Name (A-Z)"),
    Size("Size"),
}

private enum class TimeZone(
    val label: String,
) {
    Pacific("Pacific Time"),
    Mountain("Mountain Time"),
    Central("Central Time"),
    Eastern("Eastern Time"),
    UTC("UTC"),
}

/**
 * Self-contained showcase for [LiquidDropdownMenu] and [LiquidPicker].
 *
 * Android renders glass surfaces; iOS uses genuine native UIButton+UIMenu / UIPickerView.
 */
@Composable
public fun MenuShowcase(modifier: Modifier = Modifier) {
    var sort by remember { mutableStateOf(SortOrder.Newest) }
    var zone by remember { mutableStateOf(TimeZone.Pacific) }

    val sortItems =
        remember {
            SortOrder.entries.map { LiquidMenuItem(key = it, label = it.label) }
        }
    val zoneOptions =
        remember {
            TimeZone.entries.map { LiquidPickerOption(key = it, label = it.label) }
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(showcaseBackground())
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        ShowcaseTitle(
            title = "Menu & Picker",
            subtitle = "LiquidDropdownMenu and LiquidPicker — glass on Android, native on iOS.",
        )

        ShowcaseSection(title = "Dropdown menu", value = sort.label) {
            LiquidDropdownMenu(
                items = sortItems,
                onSelect = { sort = it },
                label = "Sort: ${sort.label}",
                selectedKey = sort,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        ShowcaseSection(title = "Value picker", value = zone.label) {
            LiquidPicker(
                options = zoneOptions,
                selectedKey = zone,
                onSelected = { zone = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ShowcaseTitle(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BasicText(
            text = title,
            style =
                TextStyle(
                    color = Color(0xFF070707),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                ),
        )
        BasicText(
            text = subtitle,
            style = TextStyle(color = Color(0xFF4A4A4A), fontSize = 15.sp, lineHeight = 21.sp),
        )
    }
}

@Composable
private fun ShowcaseSection(
    title: String,
    value: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BasicText(
            text = title,
            style =
                TextStyle(
                    color = Color(0xFF070707),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
        content()
        BasicText(
            text = "Selected: $value",
            style = TextStyle(color = Color(0xFF555555), fontSize = 13.sp),
        )
    }
}

private fun showcaseBackground(): Brush =
    Brush.linearGradient(
        colors =
            listOf(
                Color(0xFFF7F7F7),
                Color(0xFFEDEDED),
                Color(0xFFDADADA),
            ),
    )
