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
import com.example.petal.ui.editMemory.EditMemoryVoyagerScreen


@Composable
fun PetalBottomNavBar(
    onAddClick: () -> Unit = {} // if still needed
) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF4A2C3A)
    ) {
        NavigationBarItem(
            selected = tabNavigator.current == JournalTab,
            onClick = { tabNavigator.current = JournalTab },
            icon = { Icon(Icons.Default.Home, "Journal") },
            label = { Text("Journal") }
        )
        NavigationBarItem(
            selected = tabNavigator.current == MapTab,
            onClick = { tabNavigator.current = MapTab },
            icon = { Icon(Icons.Default.LocationOn, "Map") },
            label = { Text("Map") }
        )
        NavigationBarItem(
            selected = tabNavigator.current == CalendarTab,
            onClick = { tabNavigator.current = CalendarTab },
            icon = { Icon(Icons.Default.DateRange, "Calendar") },
            label = { Text("Calendar") }
        )
        NavigationBarItem(
            selected = tabNavigator.current == ProfileTab,
            onClick = { tabNavigator.current = ProfileTab },
            icon = { Icon(Icons.Default.Person, "Profile") },
            label = { Text("Profile") }
        )
    }
}
enum class BottomTab {
    JOURNAL, CALENDAR, MAP, PROFILE
}