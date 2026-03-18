package com.example.petal.ui.homeDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemoryDetailViewModel(
    private val repository: MemoryRepository
) : ViewModel() {
    private var currentMemoryId: Int = -1

    private val _uiState = MutableStateFlow<MemoryDetailUiState>(MemoryDetailUiState.Loading)
    val uiState: StateFlow<MemoryDetailUiState> = _uiState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun initialize(memoryId: Int) {
        currentMemoryId = memoryId
        loadMemory()
    }

    fun clearError() {
        _errorMessage.value = null
    }
    private fun loadMemory(){
        viewModelScope.launch {
            try{
                val memory = repository.getMemory(currentMemoryId)

                // ADD THIS
                println("MEMORY IMAGES COUNT: ${memory.images.size}")
                memory.images.forEach { println("IMAGE URL: ${it.imageUrl}") }

                _uiState.value = MemoryDetailUiState.Success(memory)
            } catch(e: Exception){
                _uiState.value = MemoryDetailUiState.Error(e.message ?: "Unable to load memory")
            }
        }
    }
    fun toggleFavorite() {
        val current = (_uiState.value as? MemoryDetailUiState.Success)?.memory ?: return
        viewModelScope.launch {
            try {
                if (current.isFavorite) repository.unfavorite(current.id)
                else repository.favorite(current.id)
                loadMemory()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Something went wrong"
            }
        }
    }

    fun deleteMemory(onDeleted: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteMemory(currentMemoryId)
                onDeleted()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to delete"
            }
        }
    }
}