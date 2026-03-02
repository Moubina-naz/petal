package com.example.petal.ui.mapScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.petal.NavigationEvent
import com.example.petal.data.remote.ApiProvider
import com.example.petal.ui.addMemory.AddMemoryVoyagerScreen
import com.example.petal.ui.editMemory.EditMemoryVoyagerScreen
import com.example.petal.ui.homeDetailScreen.MemoryDetailVoyagerScreen

class MapVoyagerScreen(
    private val mode: MapMode = MapMode.CREATE_MEMORY,
    private val memoryId: Int? = null,
    private val locationSource: LocationSource = LocationSource.None   // ← re-add this (key!)
) : Screen {

    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow



        MapScreen(
            mode = mode,
            locationSource = locationSource,
            onLocationPicked = { lat, lon, name ->
                val selectedSource = LocationSource.Selected(lat, lon, name)
                when (mode) {
                    MapMode.CREATE_MEMORY, MapMode.PICK_LOCATION -> {
                        navigator.replace(
                            AddMemoryVoyagerScreen(locationSource = selectedSource)
                        )
                    }
                    MapMode.EDIT_LOCATION -> {
                        if (memoryId != null) {
                            navigator.replace(
                                EditMemoryVoyagerScreen(
                                    memoryId = memoryId,
                                    locationSource = selectedSource
                                )
                            )
                        } else {
                            navigator.pop()
                        }
                    }
                }
            },
            onDismiss = { navigator.pop() }
        )
    }
}