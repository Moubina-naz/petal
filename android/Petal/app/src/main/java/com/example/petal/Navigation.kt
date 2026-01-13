package com.example.petal

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petal.Screens.EditMemoryScreen
import com.example.petal.Screens.HomeScreen
import com.example.petal.Screens.MapScreen
import com.example.petal.Screens.MemoryDetailScreen
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
    NavHost(
        navController = navController,
        startDestination = HomeScrn
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
