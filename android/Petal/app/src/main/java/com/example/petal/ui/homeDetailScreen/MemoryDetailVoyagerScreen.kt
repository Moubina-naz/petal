package com.example.petal.ui.homeDetailScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import com.example.petal.data.remote.ApiProvider
import com.example.petal.domain.Memory
import com.example.petal.ui.editMemory.EditMemoryVoyagerScreen

class MemoryDetailVoyagerScreen(
    private val memory: Memory,
) : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val viewModel= remember{MemoryDetailViewModel(ApiProvider.memoryRepository)}

        LaunchedEffect(memory) {
            viewModel.initialize(memory.id)
        }

        val navigator = LocalNavigator.currentOrThrow

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                NavigationEvent.GoBack -> {
                    navigator.pop()
                }
                is NavigationEvent.OpenEditMemory -> {
                    navigator.push(EditMemoryVoyagerScreen(event.memory ?: memory))
                }
                is NavigationEvent.DeleteMemory -> {
                    viewModel.deleteMemory {
                        navigator.pop()
                    }
                }
                is NavigationEvent.ToggleFavorite -> {
                    viewModel.toggleFavorite()
                }
                else -> {}
            }
        }

        MemoryDetailScreen(
            viewModel = viewModel,
            onNavigationEvent = onNavigationEvent
        )
    }
}