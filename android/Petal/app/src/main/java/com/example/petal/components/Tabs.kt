package com.example.petal.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.petal.LocalTabNavDepth
import com.example.petal.Screens.PetalBottomNavBar
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.calendarScreen.CalendarVoyagerScreen
import com.example.petal.ui.homeScreen.HomeVoyagerScreen
import com.example.petal.ui.mapScreen.MapVoyagerScreen
import com.example.petal.ui.profile.ProfileVoyagerScreen


object JournalTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Home)
            return remember { TabOptions(0u, "Journal", icon) }
        }

    @Composable
    override fun Content() {
        val navDepth = LocalTabNavDepth.current
        Navigator(HomeVoyagerScreen()) { navigator ->
            navDepth.value = navigator.items.size  // report depth upward
            CurrentScreen()
        }
    }
}


object MapTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.LocationOn)
            return remember { TabOptions(1u, "Map", icon) }
        }

    @Composable
    override fun Content() {
        val navDepth = LocalTabNavDepth.current

        Navigator(MapVoyagerScreen()) {navigator ->
            navDepth.value = navigator.items.size
            CurrentScreen()
        }
    }
}


object CalendarTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.DateRange)
            return remember { TabOptions(2u, "Calendar", icon) }
        }

    @Composable
    override fun Content() {
        val navDepth = LocalTabNavDepth.current
        Navigator(CalendarVoyagerScreen()) { navigator ->
            navDepth.value = navigator.items.size
            CurrentScreen()
        }
    }
}

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            return remember { TabOptions(3u, "Profile", icon) }
        }

    @Composable
    override fun Content() {
        val navDepth = LocalTabNavDepth.current
        Navigator(ProfileVoyagerScreen()) { navigator ->
            navDepth.value = navigator.items.size
            CurrentScreen()
        }
    }
}
