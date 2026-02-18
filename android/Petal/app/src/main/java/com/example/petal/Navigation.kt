package com.example.petal

/*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petal.ui.editMemory.EditMemoryScreen
import com.example.petal.ui.homeScreen.HomeScreen
import com.example.petal.mapScreen.MapScreen
import com.example.petal.ui.homeDetailScreen.MemoryDetailScreen
import com.example.petal.Screens.PetalBottomNavBar
import com.example.petal.ui.editMemory.EditMemoryViewModel
import com.example.petal.ui.homeDetailScreen.MemoryDetailViewModel
import com.example.petal.ui.homeScreen.HomeViewModel
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
                val homeVm: HomeViewModel = viewModel()
                HomeScreen(
                    navController = navController,
                    viewModel = homeVm
                )
            }

            composable<MemoryScrn> {

                val detailvm: MemoryDetailViewModel = viewModel()

                MemoryDetailScreen(
                    navController = navController,detailvm
                )
            }

            composable<AddMemoryScrn> {
                val editvm: EditMemoryViewModel = viewModel()

                EditMemoryScreen(
                    navController = navController,editvm
                )
            }

            composable<EditMemoryScrn> {
                val editvm: EditMemoryViewModel = viewModel()

                EditMemoryScreen(
                    navController = navController,editvm
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
*/