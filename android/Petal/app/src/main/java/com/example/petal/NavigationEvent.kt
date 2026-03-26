package com.example.petal

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import com.example.petal.domain.Memory
import com.example.petal.ui.mapScreen.LocationSource

sealed class NavigationEvent {

    data object GoBack : NavigationEvent()

    data class OpenMemoryDetail(val memoryId: Int) : NavigationEvent()
    data class OpenEditMemory(val memoryId: Int) : NavigationEvent()
    data class OpenAddMemory(
        val locationSource: LocationSource = LocationSource.None
    ) : NavigationEvent()
    data object OpenMap : NavigationEvent()
    data object OpenSettings : NavigationEvent()
    data class OpenAddMemoryWithLocation(
        val latitude: Double,
        val longitude: Double,
        val name: String?
    ) : NavigationEvent()



    // Memory
    data class ToggleFavorite(val memoryId: Int) : NavigationEvent()
    data class DeleteMemory(val memoryId: Int) : NavigationEvent()

    // Edit Memory
    data object SaveMemory : NavigationEvent()
    data object CancelEdit : NavigationEvent()
    data class RemoveImage(val imageId: String) : NavigationEvent()
    data object AddImage : NavigationEvent()
}
val LocalTabNavDepth = compositionLocalOf { mutableStateOf(1) }
