package com.example.petal.ui.addMemory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import com.example.petal.data.remote.ApiProvider

class AddMemoryVoyagerScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val context = LocalContext.current
        // Get ViewModel using remember (no Koin needed)
        val viewModel = remember {
            AddMemoryViewModel(ApiProvider.memoryRepository, context)
        }

        val navigator = LocalNavigator.currentOrThrow

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                NavigationEvent.GoBack -> navigator.pop()
                NavigationEvent.SaveMemory -> {
                    viewModel.save { navigator.pop() }
                }
                else -> {}
            }
        }

        AddMemoryScreen(
            viewModel = viewModel,
            onNavigationEvent = onNavigationEvent
        )
    }
}