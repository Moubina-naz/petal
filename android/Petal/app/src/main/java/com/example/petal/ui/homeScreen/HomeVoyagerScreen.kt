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
        LaunchedEffect(Unit) {
            var nav: cafe.adriel.voyager.navigator.Navigator? = navigator
            var depth = 0
            while (nav != null) {
                android.util.Log.d("NAV_DEBUG", "depth $depth: ${nav.lastItem::class.simpleName}")
                nav = nav.parent
                depth++
            }
        }
        LaunchedEffect(navigator.lastItem) {
            homeViewModel.loadMemories()
        }

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                is NavigationEvent.OpenMemoryDetail ->
                    navigator.push(              // ← parentNavigator
                        MemoryDetailVoyagerScreen(event.memoryId)
                    )

                is NavigationEvent.OpenEditMemory ->
                    navigator.push(              // ← parentNavigator
                        EditMemoryVoyagerScreen(event.memoryId)
                    )
                is NavigationEvent.OpenSettings ->
                    navigator.push(SettingsVoyagerScreen())  // ← parentNavigator

                is NavigationEvent.OpenAddMemory ->
                    navigator.push(AddMemoryVoyagerScreen()) // ← parentNavigator

                NavigationEvent.OpenMap ->
                    navigator.push(MapVoyagerScreen())       // ← parentNavigator

                NavigationEvent.GoBack ->
                    navigator.pop()  // ← keep as navigator (pop within tab is fine)

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
