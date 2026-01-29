package com.example.petal

import com.example.petal.domain.Memory

sealed class NavigationEvent {
    data object GoBack : NavigationEvent()

    // Screen navigation
    data class OpenMemoryDetail(val memory: Memory) : NavigationEvent()
    data class OpenEditMemory(val memory: Memory? = null) : NavigationEvent()
    data object OpenAddMemory : NavigationEvent()
    data object OpenMap : NavigationEvent()

    // Bottom navigation
    data object NavigateToJournal : NavigationEvent()
    data object NavigateToMap : NavigationEvent()
    data object NavigateToCalendar : NavigationEvent()
    data object NavigateToProfile : NavigationEvent()
    // Memory actions
    data class ToggleFavorite(val memory: Memory) : NavigationEvent()
    data class DeleteMemory(val memory: Memory) : NavigationEvent()

    // Edit Memory actions
    data object SaveMemory : NavigationEvent()
    data object CancelEdit : NavigationEvent()
    data class RemoveImage(val imageId: String) : NavigationEvent()
    data object AddImage : NavigationEvent()
}