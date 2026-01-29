package com.example.petal.Screens
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import com.example.petal.ui.homeDetailScreen.MemoryDetailVoyagerScreen

class MapVoyagerScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                is NavigationEvent.OpenMemoryDetail -> {
                    navigator.push(MemoryDetailVoyagerScreen(event.memory))
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