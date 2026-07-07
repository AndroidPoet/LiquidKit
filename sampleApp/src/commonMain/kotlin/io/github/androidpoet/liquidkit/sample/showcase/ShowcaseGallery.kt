package io.github.androidpoet.liquidkit.sample.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The full set of self-contained component showcases, identified for navigation.
 *
 * Each entry maps a human-readable title/summary to the showcase composable that
 * renders that component group. Android renders the Liquid Glass shader engine;
 * iOS wraps genuine native UIKit controls.
 */
public enum class ShowcaseEntry(
    public val title: String,
    public val summary: String,
) {
    Button("Buttons & FAB", "LiquidButton variants and LiquidFab"),
    Surface("Surfaces", "LiquidSurface and LiquidGlassContainer"),
    Menu("Menus & Pickers", "LiquidDropdownMenu and LiquidPicker"),
    Field("Fields", "LiquidTextField and LiquidSearchField"),
    StepperMedia("Stepper & Media", "LiquidStepper and LiquidMediaControl"),
    Chrome("Toolbar & Sheet", "LiquidToolbar and LiquidSheet"),
}

/** Renders the showcase composable for the given [entry]. */
@Composable
public fun ShowcaseEntryContent(entry: ShowcaseEntry, modifier: Modifier = Modifier) {
    when (entry) {
        ShowcaseEntry.Button -> Box(modifier.fillMaxSize()) { ButtonShowcase() }
        ShowcaseEntry.Surface -> SurfaceShowcase(modifier)
        ShowcaseEntry.Menu -> MenuShowcase(modifier)
        ShowcaseEntry.Field -> FieldShowcase(modifier)
        ShowcaseEntry.StepperMedia -> Box(modifier.fillMaxSize()) { StepperMediaShowcase() }
        ShowcaseEntry.Chrome -> ChromeShowcase(modifier)
    }
}

/**
 * A self-contained gallery that lists every component showcase and opens the
 * selected one full-screen with a back affordance. Drop it into any host; it owns
 * its own selection state and demonstrates the entire LiquidKit component set.
 */
@Composable
public fun ShowcaseGallery(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf<ShowcaseEntry?>(null) }
    val current = selected

    if (current == null) {
        ShowcaseGalleryList(
            modifier = modifier,
            onSelect = { selected = it },
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            ShowcaseEntryContent(current, modifier = Modifier.fillMaxSize())
            BackChip(
                onClick = { selected = null },
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .safeDrawingPadding()
                        .padding(16.dp),
            )
        }
    }
}

@Composable
private fun ShowcaseGalleryList(
    onSelect: (ShowcaseEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BasicText(
            text = "Component gallery",
            style =
                TextStyle(
                    color = Color(0xFF070707),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                ),
        )
        BasicText(
            text = "Open any component group full-screen. Every entry is a self-contained showcase.",
            style =
                TextStyle(
                    color = Color(0xFF555555),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                ),
        )
        ShowcaseEntry.entries.forEach { entry ->
            GalleryRow(entry = entry, onClick = { onSelect(entry) })
        }
    }
}

@Composable
private fun GalleryRow(
    entry: ShowcaseEntry,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F3F3), RoundedCornerShape(18.dp))
                .border(1.dp, Color(0x14000000), RoundedCornerShape(18.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            BasicText(
                text = entry.title,
                style =
                    TextStyle(
                        color = Color(0xFF070707),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
            )
            BasicText(
                text = entry.summary,
                style = TextStyle(color = Color(0xFF666666), fontSize = 13.sp),
            )
        }
        BasicText(
            text = "Open",
            style =
                TextStyle(
                    color = Color(0xFF1B6BF2),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}

@Composable
private fun BackChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(Color(0xCC0A0A0A), RoundedCornerShape(20.dp))
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 18.dp, vertical = 10.dp),
    ) {
        BasicText(
            text = "‹ Back",
            style =
                TextStyle(
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}
