package com.example.petal.ui.mapScreen
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import com.example.petal.ui.addMemory.AddMemoryVoyagerScreen
import com.example.petal.ui.homeDetailScreen.MemoryDetailVoyagerScreen

class MapVoyagerScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                is NavigationEvent.OpenAddMemoryWithLocation -> {
                    navigator.push(
                        AddMemoryVoyagerScreen(
                            locationSource = LocationSource.Selected(
                                latitude = event.latitude,
                                longitude = event.longitude,
                                name = event.name
                            )
                        )
                    )

                }

                NavigationEvent.GoBack -> {
                    navigator.pop()
                }
                else -> {}
            }
        }

        MapScreen(
            onNavigationEvent = onNavigationEvent
        )
    }
}