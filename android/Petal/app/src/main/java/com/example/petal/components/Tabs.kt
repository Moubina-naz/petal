package com.example.petal.components


import HomeVoyagerScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.petal.ui.mapScreen.MapScreen
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.homeScreen.HomeViewModel
import com.example.petal.ui.mapScreen.MapViewModel
import com.example.petal.ui.mapScreen.MapVoyagerScreen

object JournalTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val iconPainter = rememberVectorPainter(Icons.Default.Home)
            return remember {  // remember the TabOptions instance itself
                TabOptions(
                    index = 0u,
                    title = "Journal",
                    icon = iconPainter
                )
            }
        }

    @Composable
    override fun Content() {
        val homeViewModel = remember { HomeViewModel(ApiProvider.memoryRepository) }
        Navigator(HomeVoyagerScreen())
    }
}

object MapTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val iconPainter = rememberVectorPainter(Icons.Default.LocationOn)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Map",
                    icon = iconPainter
                )
            }
        }

    @Composable
    override fun Content() {
        val mapViewModel = remember { MapViewModel(ApiProvider.memoryRepository) }
        Navigator(MapVoyagerScreen())
    }
}

object CalendarTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val iconPainter = rememberVectorPainter(Icons.Default.DateRange)  // or whatever icon you want
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Calendar",
                    icon = iconPainter
                )
            }
        }

    @Composable
    override fun Content() {
        Text("Calendar Screen")  // replace with real content
    }
}

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val iconPainter = rememberVectorPainter(Icons.Default.Person)
            return remember {
                TabOptions(
                    index = 3u,
                    title = "Profile",
                    icon = iconPainter
                )
            }
        }

    @Composable
    override fun Content() {
        Text("Profile Screen")  // replace with real content
    }
}