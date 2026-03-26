package com.example.petal.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.example.petal.components.CalendarTab
import com.example.petal.components.JournalTab
import com.example.petal.components.MapTab
import com.example.petal.components.ProfileTab

@Composable
fun PetalBottomNavBar() {

    val tabNavigator = LocalTabNavigator.current
    val currentTab = runCatching { tabNavigator.current }.getOrNull()

    val selectedColor = Color(0xFFE86A33)
    val unselectedColor = Color(0xFFBDBDBD)
    val navigator = LocalNavigator.currentOrThrow
    val isTopLevel = navigator.items.size == 1

    AnimatedVisibility(
        visible = isTopLevel,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
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
                selected = currentTab == JournalTab,
                onClick = {
                    if (tabNavigator.current == JournalTab) {
                        navigator.popUntilRoot()
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
                selected = currentTab == MapTab,
                onClick = {
                    if (tabNavigator.current == MapTab) {
                        navigator.popUntilRoot()
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
                selected = currentTab == CalendarTab,
                onClick = {
                    if (tabNavigator.current == CalendarTab) {
                        navigator.popUntilRoot()
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
                selected = currentTab == ProfileTab,
                onClick = {
                    if (tabNavigator.current == ProfileTab) {
                        navigator.popUntilRoot()
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
}