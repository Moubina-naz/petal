package com.example.petal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petal.Screens.EditMemoryScreen
import com.example.petal.Screens.HomeScreen
import com.example.petal.Screens.MapScreen
import com.example.petal.Screens.MemoryDetailScreen
import com.example.petal.Screens.PetalBottomNavBar
import kotlinx.serialization.Serializable

@Serializable
object HomeScrn

@Serializable
object MemoryScrn

@Serializable
object AddMemoryScrn

@Serializable
object EditMemoryScrn

@Serializable
object MapScrn

@Serializable
object Settings

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""

    val shouldShowBottomBar = when {
        currentRoute.contains("Login", ignoreCase = true) -> false
        currentRoute.contains("SignUp", ignoreCase = true) -> false
        currentRoute.contains("Splash", ignoreCase = true) -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                PetalBottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeScrn,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable<HomeScrn> {
                HomeScreen(
                    navController = navController
                )
            }

            composable<MemoryScrn> {
                MemoryDetailScreen(
                    navController = navController
                )
            }

            composable<AddMemoryScrn> {
                EditMemoryScreen(
                    navController = navController
                )
            }

            composable<EditMemoryScrn> {
                EditMemoryScreen(
                    navController = navController
                )
            }

            composable<MapScrn> {
                MapScreen(
                    navController = navController
                )
            }


        }
    }
}
