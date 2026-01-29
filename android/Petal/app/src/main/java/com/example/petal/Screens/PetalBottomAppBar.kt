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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.petal.ui.editMemory.EditMemoryVoyagerScreen


@Composable
fun PetalBottomNavBar(
    modifier: Modifier = Modifier,
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Box(modifier = modifier) {
        BottomAppBar(
            containerColor = Color(0xFFF7F4EE),
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    label = "Journal",
                    icon = Icons.Default.Email,
                    selected = selectedTab == BottomTab.JOURNAL,
                    onClick = { onTabSelected(BottomTab.JOURNAL) }
                )

                BottomNavItem(
                    label = "Map",
                    icon = Icons.Default.Place,
                    selected = selectedTab == BottomTab.MAP,
                    onClick = { onTabSelected(BottomTab.MAP) }
                )

                Spacer(modifier = Modifier.width(48.dp))

                BottomNavItem(
                    label = "Calendar",
                    icon = Icons.Default.Call,
                    selected = selectedTab == BottomTab.CALENDAR,
                    onClick = { onTabSelected(BottomTab.CALENDAR) }
                )

                BottomNavItem(
                    label = "Profile",
                    icon = Icons.Default.Person,
                    selected = selectedTab == BottomTab.PROFILE,
                    onClick = { onTabSelected(BottomTab.PROFILE) }
                )
            }
        }

        FloatingActionButton(
            onClick = {
                navigator.push(EditMemoryVoyagerScreen())
            },
            containerColor = Color(0xFF4A2C3A),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}

enum class BottomTab {
    JOURNAL, CALENDAR, MAP, PROFILE
}