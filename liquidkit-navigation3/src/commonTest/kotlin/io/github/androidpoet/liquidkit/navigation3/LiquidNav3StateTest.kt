package io.github.androidpoet.liquidkit.navigation3

import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LiquidNav3StateTest {
    @Test
    fun test_selectTopLevel_routeInSet_updatesSelectedTopLevelRoute() {
        val state = navState()

        state.selectTopLevel(SearchRoute)

        assertEquals(SearchRoute, state.selectedTopLevelRoute)
    }

    @Test
    fun test_select_nonTopLevelRoute_addsToCurrentBackStack() {
        val state = navState()

        state.select(DetailRoute)

        assertEquals(listOf(HomeRoute, DetailRoute), state.currentBackStack.toList())
    }

    @Test
    fun test_backStack_topLevelRoute_allowsNestedStackControl() {
        val state = navState()

        state.backStack(SearchRoute).add(DetailRoute)

        assertEquals(listOf(SearchRoute, DetailRoute), state.backStack(SearchRoute).toList())
        assertEquals(listOf(HomeRoute), state.currentBackStack.toList())
    }

    @Test
    fun test_pop_detailRoute_removesFromCurrentBackStack() {
        val state = navState()
        state.navigate(DetailRoute)

        val popped = state.pop()

        assertTrue(popped)
        assertEquals(listOf(HomeRoute), state.currentBackStack.toList())
    }

    @Test
    fun test_pop_nonStartTopLevel_returnsToStartRoute() {
        val state = navState(selectedRoute = SearchRoute)

        val popped = state.pop()

        assertTrue(popped)
        assertEquals(HomeRoute, state.selectedTopLevelRoute)
    }

    @Test
    fun test_pop_startRoute_returnsFalse() {
        val state = navState()

        val popped = state.pop()

        assertFalse(popped)
        assertEquals(HomeRoute, state.selectedTopLevelRoute)
    }

    private fun navState(selectedRoute: NavKey = HomeRoute): LiquidNav3State =
        LiquidNav3State(
            startRoute = HomeRoute,
            topLevelRoutes = setOf(HomeRoute, SearchRoute),
            selectedRoute = mutableStateOf(selectedRoute),
            backStacks =
                mapOf(
                    HomeRoute to NavBackStack(HomeRoute),
                    SearchRoute to NavBackStack(SearchRoute),
                ),
        )
}

@Serializable
private data object HomeRoute : NavKey

@Serializable
private data object SearchRoute : NavKey

@Serializable
private data object DetailRoute : NavKey
