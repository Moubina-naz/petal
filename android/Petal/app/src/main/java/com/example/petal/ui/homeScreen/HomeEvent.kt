package com.example.petal.ui.homeScreen

sealed interface HomeEvent {
    data class MemoryClicked(val memoryId: Int) : HomeEvent
    object SettingsClicked : HomeEvent
    object AddMemoryClicked : HomeEvent
    data class SearchChanged(val query: String) : HomeEvent
    data class FilterChanged(val filter: HomeFilter) : HomeEvent
}