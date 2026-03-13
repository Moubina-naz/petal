package com.example.petal.ui.homeScreen


import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.example.petal.NavigationEvent
import com.example.petal.ui.mapScreen.MapVoyagerScreen
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.editMemory.EditMemoryVoyagerScreen
import com.example.petal.ui.homeDetailScreen.MemoryDetailVoyagerScreen
import com.example.petal.ui.homeScreen.HomeViewModel
import com.example.petal.ui.addMemory.AddMemoryVoyagerScreen
import com.example.petal.ui.homeScreen.HomeScreen
import com.example.petal.ui.settings.SettingsVoyagerScreen


class HomeVoyagerScreen : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val homeViewModel = remember {
            HomeViewModel(ApiProvider.memoryRepository)
        }

        LaunchedEffect(navigator.lastItem) {
            homeViewModel.loadMemories()
        }

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                is NavigationEvent.OpenMemoryDetail ->
                    navigator.push(
                        MemoryDetailVoyagerScreen(event.memoryId)
                    )

                is NavigationEvent.OpenEditMemory ->
                    navigator.push(
                        EditMemoryVoyagerScreen(event.memoryId)
                    )
                is NavigationEvent.OpenSettings ->
                    navigator.push(SettingsVoyagerScreen())
                is NavigationEvent.OpenAddMemory ->
                    navigator.push(AddMemoryVoyagerScreen())

                NavigationEvent.OpenMap ->
                    navigator.push(MapVoyagerScreen())

                NavigationEvent.GoBack ->
                    navigator.pop()

                is NavigationEvent.ToggleFavorite ->
                    homeViewModel.favoriteById(event.memoryId)

                else -> {}
            }
        }

        HomeScreen(
            viewModel = homeViewModel,
            onNavigationEvent = onNavigationEvent
        )
    }
}
