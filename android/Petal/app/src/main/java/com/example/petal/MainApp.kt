// MainApp.kt
package com.example.petal

import HomeVoyagerScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.petal.Screens.BottomTab
import com.example.petal.Screens.MapScreen
import com.example.petal.Screens.MapVoyagerScreen
import com.example.petal.Screens.PetalBottomNavBar
import com.example.petal.ui.homeScreen.HomeScreen

@Composable
fun MainApp() {
    var selectedTab by rememberSaveable { mutableStateOf(BottomTab.JOURNAL) }

    Scaffold(
        bottomBar = {
            PetalBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                },
                onAddClick = {
                    // Handle add click
                }
            )
        },
        floatingActionButton = {
            // Optional: Move FAB here if you want it separate from bottom bar
        }
    ) { innerPadding ->
        // Show screen based on selected tab
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFDF8F4))
        ) {
            when (selectedTab) {
                BottomTab.JOURNAL -> HomeScreen(
                    viewModel = viewModel(),
                    onNavigationEvent = { /* handle events */ }
                )
                BottomTab.MAP -> MapScreen()
                BottomTab.CALENDAR -> Text("Calendar")
                BottomTab.PROFILE -> Text("Profile")
            }
        }
    }
}
