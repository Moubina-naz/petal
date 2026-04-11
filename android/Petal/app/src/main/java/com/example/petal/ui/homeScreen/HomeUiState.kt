package com.example.petal.ui.homeScreen

import com.example.petal.components.MemorySection
import com.example.petal.domain.Memory

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Connecting(val attempt: Int) : HomeUiState    data class Success(val memories : List<Memory>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}