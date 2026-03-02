package com.example.petal.ui.addMemory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.mapScreen.LocationSource
import com.example.petal.ui.mapScreen.MapMode
import com.example.petal.ui.mapScreen.MapVoyagerScreen

class AddMemoryVoyagerScreen(
    private val locationSource: LocationSource = LocationSource.None
) : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel: AddMemoryViewModel = remember(locationSource) {
            AddMemoryViewModel(
                repository = ApiProvider.memoryRepository,
                context = context,
                locationSource = locationSource
            )
        }

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(viewModel) {
            viewModel.saveEffects.collect { effect ->
                when (effect) {
                    is SaveEffect.NavigateBack -> {
                        navigator.popUntilRoot()
                    }
                    is SaveEffect.Error -> {}
                }
            }
        }

        AddMemoryScreen(
            viewModel = viewModel,
            onNavigationEvent = { event ->
                when (event) {
                    NavigationEvent.GoBack -> navigator.pop()
                    NavigationEvent.SaveMemory -> viewModel.save()
                    NavigationEvent.OpenMap -> {
                        navigator.push(MapVoyagerScreen(mode = MapMode.PICK_LOCATION))
                    }

                    else -> {}
                }
            }
        )
    }
}