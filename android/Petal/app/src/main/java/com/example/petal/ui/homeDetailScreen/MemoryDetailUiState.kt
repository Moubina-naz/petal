package com.example.petal.ui.homeDetailScreen

import com.example.petal.domain.Memory

sealed interface MemoryDetailUiState {
    object Loading : MemoryDetailUiState
    data class Success(val memory: Memory) : MemoryDetailUiState
    data class Error(val message: String) : MemoryDetailUiState

}