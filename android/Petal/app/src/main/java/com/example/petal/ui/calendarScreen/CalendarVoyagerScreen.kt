package com.example.petal.ui.calendarScreen


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.homeDetailScreen.MemoryDetailVoyagerScreen

class CalendarVoyagerScreen : Screen {

    override val key = "CalendarScreen"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = remember { CalendarViewModel(ApiProvider.memoryRepository) }

        CalendarScreen(
            viewModel = viewModel,
            onNavigationEvent = { event ->
                when (event) {
                    is NavigationEvent.OpenMemoryDetail -> {
                        navigator.push(MemoryDetailVoyagerScreen(event.memoryId))
                    }
                    else -> {}
                }
            }
        )
    }
}