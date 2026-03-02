package com.example.petal.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.example.petal.components.BottomNavItem
import com.example.petal.components.CalendarTab
import com.example.petal.components.JournalTab
import com.example.petal.components.MapTab
import com.example.petal.components.ProfileTab


@Composable
fun PetalBottomNavBar(
    onAddClick: () -> Unit = {} // keep if you need it later
) {
    val tabNavigator = LocalTabNavigator.current
    val navigator = LocalNavigator.currentOrThrow  // this gets the current (possibly nested) navigator

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF4A2C3A)
    ) {
        // Journal / Home
        NavigationBarItem(
            selected = tabNavigator.current == JournalTab,
            onClick = {
                if (tabNavigator.current == JournalTab) {
                    // Re-click → pop everything until root (home screen)
                    while (navigator.canPop) {
                        navigator.pop()
                    }
                } else {
                    tabNavigator.current = JournalTab
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Journal") },
            label = { Text("Journal") }
        )

        // Map
        NavigationBarItem(
            selected = tabNavigator.current == MapTab,
            onClick = {
                if (tabNavigator.current == MapTab) {
                    while (navigator.canPop) {
                        navigator.pop()
                    }
                } else {
                    tabNavigator.current = MapTab
                }
            },
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Map") },
            label = { Text("Map") }
        )

        // Calendar
        NavigationBarItem(
            selected = tabNavigator.current == CalendarTab,
            onClick = {
                if (tabNavigator.current == CalendarTab) {
                    while (navigator.canPop) {
                        navigator.pop()
                    }
                } else {
                    tabNavigator.current = CalendarTab
                }
            },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
            label = { Text("Calendar") }
        )

        // Profile
        NavigationBarItem(
            selected = tabNavigator.current == ProfileTab,
            onClick = {
                if (tabNavigator.current == ProfileTab) {
                    while (navigator.canPop) {
                        navigator.pop()
                    }
                } else {
                    tabNavigator.current = ProfileTab
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") })
    }
}

enum class BottomTab {
    JOURNAL, CALENDAR, MAP, PROFILE
}