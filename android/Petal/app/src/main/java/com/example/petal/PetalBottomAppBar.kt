package com.example.petal

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.DateRange
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


@Composable
fun PetalBottomNavBar() {
    var selectedTab by rememberSaveable { mutableStateOf(BottomTab.JOURNAL) }

    Box {
        BottomAppBar(
            containerColor = Color(0xFFF6F1EA),
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                BottomNavItem(
                    label = "Journal",
                    icon = Icons.Default.Email,
                    selected = selectedTab == BottomTab.JOURNAL,
                    onClick = { selectedTab = BottomTab.JOURNAL }
                )

                BottomNavItem(
                    label = "Calendar",
                    icon = Icons.Default.ShoppingCart,
                    selected = selectedTab == BottomTab.CALENDAR,
                    onClick = { selectedTab = BottomTab.CALENDAR }
                )

                BottomNavItem(
                    label = "Map",
                    icon = Icons.Default.Place,
                    selected = selectedTab == BottomTab.MAP,
                    onClick = { selectedTab = BottomTab.MAP }
                )
            }
        }

        // Floating Add button — bottom right (like image)
        FloatingActionButton(
            onClick = { /* later */ },
            containerColor = Color(0xFFB07A7A),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-24).dp, y = (-28).dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add memory",
                tint = Color.White
            )
        }
    }
}


enum class BottomTab {
    JOURNAL, CALENDAR, MAP, PROFILE
}