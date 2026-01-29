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

    fun initialize(memoryId: Int) {
        currentMemoryId = memoryId
        loadMemory()
    }


    private fun loadMemory(){
        viewModelScope.launch {
            try{
                val memory = repository.getMemory(currentMemoryId)
                _uiState.value = MemoryDetailUiState.Success(memory)
            } catch(e: Exception){
                _uiState.value = MemoryDetailUiState.Error(
                    e.message ?: "Unable to load memory"
                )
            }
        }
    }
    fun toggleFavorite() {
        val current = (_uiState.value as? MemoryDetailUiState.Success)?.memory
            ?: return

        viewModelScope.launch {
            if (current.isFavorite)
                repository.unfavorite(current.id)
            else
                repository.favorite(current.id)

            loadMemory()
        }
    }

    fun deleteMemory(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteMemory(currentMemoryId)
            onDeleted()
        }
    }
}