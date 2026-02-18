package com.example.petal.ui.editMemory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import cafe.adriel.voyager.core.screen.Screen
import com.example.petal.data.remote.ApiProvider
import com.example.petal.domain.Memory

class EditMemoryVoyagerScreen(
    private val memoryId: Int
) : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val context = LocalContext.current

        val viewModel = remember(memoryId) {
            EditMemoryViewModel(
                ApiProvider.memoryRepository,
                context
            )
        }


        LaunchedEffect(memoryId) {
            viewModel.initialize(memoryId.toString())
        }

        val navigator = LocalNavigator.currentOrThrow

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {
                NavigationEvent.GoBack,
                NavigationEvent.CancelEdit -> navigator.pop()

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

                else -> {}
            }
        }

        EditMemoryScreen(
            viewModel = viewModel,
            onNavigationEvent = onNavigationEvent
        )
    }
}
