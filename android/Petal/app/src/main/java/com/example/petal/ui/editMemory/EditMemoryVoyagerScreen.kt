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
import com.example.petal.ui.mapScreen.LocationSource
import com.example.petal.ui.mapScreen.MapMode
import com.example.petal.ui.mapScreen.MapVoyagerScreen

class EditMemoryVoyagerScreen(
    private val memoryId: Int,
    private val locationSource: LocationSource = LocationSource.None
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

        LaunchedEffect(Unit) {
            viewModel.initialize(
                memoryId = memoryId.toString(),
                locationSource = locationSource
            )
        }

        val navigator = LocalNavigator.currentOrThrow

        val onNavigationEvent: (NavigationEvent) -> Unit = { event ->
            when (event) {

                NavigationEvent.GoBack -> navigator.pop()
                NavigationEvent.CancelEdit -> navigator.pop()  // ← parentNavigator

                NavigationEvent.SaveMemory -> {
                    viewModel.save {
                        navigator.pop()  // ← parentNavigator
                    }
                }

                is NavigationEvent.RemoveImage -> {
                    viewModel.removeImageById(event.imageId)
                }

                NavigationEvent.AddImage -> {
                    viewModel.addImage("content://dummy-image-${System.currentTimeMillis()}")
                }

                NavigationEvent.OpenMap -> {
                    val state = viewModel.uiState.value
                    val currentLocationSource =
                        if (state.latitude != null && state.longitude != null) {
                            LocationSource.Selected(
                                latitude = state.latitude!!,
                                longitude = state.longitude!!,
                                name = state.locationName.takeIf { it.isNotBlank() }
                            )
                        } else {
                            LocationSource.None
                        }

                    navigator.push(  // ← parentNavigator
                        MapVoyagerScreen(
                            mode = MapMode.EDIT_LOCATION,
                            memoryId = memoryId,
                            locationSource = currentLocationSource
                        )
                    )
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