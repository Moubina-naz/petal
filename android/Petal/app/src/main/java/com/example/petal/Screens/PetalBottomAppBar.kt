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
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
import com.example.petal.components.TabNavigatorStore


@Composable
fun PetalBottomNavBar(
    onAddClick: () -> Unit = {}
) {
    val tabNavigator = LocalTabNavigator.current

    val selectedColor = Color(0xFFE86A33)
    val unselectedColor = Color(0xFFBDBDBD)

    NavigationBar(
        modifier = Modifier.drawBehind {
            val strokeWidth = 2.dp.toPx()
            drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = strokeWidth
            )
        },
        containerColor = Color.White
    ) {

        NavigationBarItem(
            selected = tabNavigator.current == JournalTab,
            onClick = {
                if (tabNavigator.current == JournalTab) {
                    TabNavigatorStore.resetTab(JournalTab)
                } else {
                    tabNavigator.current = JournalTab
                }
            },
            icon = { Icon(Icons.Outlined.Book, contentDescription = "Journal") },
            label = { Text("Journal") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = tabNavigator.current == MapTab,
            onClick = {
                if (tabNavigator.current == MapTab) {
                    TabNavigatorStore.resetTab(MapTab)
                } else {
                    tabNavigator.current = MapTab
                }
            },
            icon = { Icon(Icons.Outlined.Place, contentDescription = "Places") },
            label = { Text("Places") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = tabNavigator.current == CalendarTab,
            onClick = {
                if (tabNavigator.current == CalendarTab) {
                    TabNavigatorStore.resetTab(CalendarTab)
                } else {
                    tabNavigator.current = CalendarTab
                }
            },
            icon = { Icon(Icons.Outlined.CalendarToday, contentDescription = "Time") },
            label = { Text("Time") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = tabNavigator.current == ProfileTab,
            onClick = {
                if (tabNavigator.current == ProfileTab) {
                    TabNavigatorStore.resetTab(ProfileTab)
                } else {
                    tabNavigator.current = ProfileTab
                }
            },
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = unselectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            )
        )
    }
}

enum class BottomTab {
    JOURNAL, CALENDAR, MAP, PROFILE
}