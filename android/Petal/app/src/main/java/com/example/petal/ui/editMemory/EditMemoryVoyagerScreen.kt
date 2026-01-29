package com.example.petal.ui.editMemory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import cafe.adriel.voyager.core.screen.Screen
import com.example.petal.data.remote.ApiProvider
import com.example.petal.domain.Memory

class EditMemoryVoyagerScreen(
    private val memory: Memory? = null,
) : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {

        val viewModel = remember {
            EditMemoryViewModel(ApiProvider.memoryRepository)
        }
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(memory) {
            val memoryId = memory?.id?.toString() ?: ""
            viewModel.initialize(memoryId)
        }

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                NavigationEvent.GoBack -> {
                    navigator.pop()
                }
                NavigationEvent.SaveMemory -> {
                    viewModel.save {
                        navigator.pop()
                    }
                }
                is NavigationEvent.RemoveImage -> {
                    viewModel.removeImageById(event.imageId)
                }
                NavigationEvent.AddImage -> {
                    viewModel.addImage("content://dummy-image-${System.currentTimeMillis()}")
                }
                NavigationEvent.CancelEdit -> {
                    navigator.pop()
                }
                else -> {}
            }
        }

        EditMemoryScreen(
            viewModel = viewModel,
            onNavigationEvent = onNavigationEvent,)
    }
}