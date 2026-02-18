package com.example.petal.ui.mapScreen

import com.example.petal.domain.Memory

sealed interface MapUiState {
    object Loading : MapUiState
    data class Success(val memories: List<Memory>) : MapUiState
    data class Error(val message: String) : MapUiState
}
