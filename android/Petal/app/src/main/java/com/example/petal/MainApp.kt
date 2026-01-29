// MainApp.kt
package com.example.petal

import HomeVoyagerScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.petal.Screens.BottomTab
import com.example.petal.Screens.MapVoyagerScreen
import com.example.petal.Screens.PetalBottomNavBar

@Composable
fun MainApp() {

    var selectedTab by rememberSaveable { mutableStateOf(BottomTab.JOURNAL) }

    Navigator(HomeVoyagerScreen()) { navigator ->

        Box(modifier = Modifier.fillMaxSize()) {

            // Screen transitions
            SlideTransition(navigator)

            // Bottom bar OVERLAY
            PetalBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab

                    when (tab) {
                        BottomTab.JOURNAL -> navigator.replace(HomeVoyagerScreen())
                        BottomTab.MAP -> navigator.replace(MapVoyagerScreen())
                        BottomTab.CALENDAR -> { /* later */ }
                        BottomTab.PROFILE -> { /* later */ }
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
