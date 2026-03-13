package com.example.petal.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.petal.ui.mapScreen.MapScreen
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.calendarScreen.CalendarVoyagerScreen
import com.example.petal.ui.homeScreen.HomeViewModel
import com.example.petal.ui.homeScreen.HomeVoyagerScreen
import com.example.petal.ui.mapScreen.MapViewModel
import com.example.petal.ui.mapScreen.MapVoyagerScreen
import com.example.petal.ui.profile.ProfileVoyagerScreen


object TabNavigatorStore {
    private val navigators = mutableMapOf<Tab, Navigator>()

    fun register(tab: Tab, navigator: Navigator) {
        navigators[tab] = navigator
    }
    fun resetAll() {
        navigators.values.forEach { it.popUntilRoot() }
    }
    fun resetTab(tab: Tab) {
        navigators[tab]?.popUntilRoot()
    }
    fun clear() {   // ADD THIS
        navigators.clear()
    }
}
object JournalTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val iconPainter = rememberVectorPainter(Icons.Default.Home)
            return remember {
                TabOptions(index = 0u, title = "Journal", icon = iconPainter)
            }
        }

    @Composable
    override fun Content() {
        val homeViewModel = remember { HomeViewModel(ApiProvider.memoryRepository) }
        Navigator(HomeVoyagerScreen()) { navigator ->
            LaunchedEffect(navigator) {
                TabNavigatorStore.register(JournalTab, navigator)
            }
            cafe.adriel.voyager.navigator.CurrentScreen()
        }
    }
}

object MapTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val iconPainter = rememberVectorPainter(Icons.Default.LocationOn)
            return remember {
                TabOptions(index = 1u, title = "Map", icon = iconPainter)
            }
        }

    @Composable
    override fun Content() {
        val mapViewModel = remember { MapViewModel(ApiProvider.memoryRepository) }
        Navigator(MapVoyagerScreen()) { navigator ->
            LaunchedEffect(navigator) {
                TabNavigatorStore.register(MapTab, navigator)
            }
            cafe.adriel.voyager.navigator.CurrentScreen()
        }
    }
}

object CalendarTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val iconPainter = rememberVectorPainter(Icons.Default.DateRange)
            return remember {
                TabOptions(index = 2u, title = "Calendar", icon = iconPainter)
            }
        }

    @Composable
    override fun Content() {
        Navigator(CalendarVoyagerScreen()) { navigator ->
            LaunchedEffect(navigator) {
                TabNavigatorStore.register(CalendarTab, navigator)
            }
            cafe.adriel.voyager.navigator.CurrentScreen()
        }
    }
}

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val iconPainter = rememberVectorPainter(Icons.Default.Person)
            return remember {
                TabOptions(index = 3u, title = "Profile", icon = iconPainter)
            }
        }

    @Composable
    override fun Content() {
        Navigator(ProfileVoyagerScreen()) { navigator ->
            LaunchedEffect(navigator) {
                TabNavigatorStore.register(ProfileTab, navigator)
            }
            cafe.adriel.voyager.navigator.CurrentScreen()
        }
    }
}