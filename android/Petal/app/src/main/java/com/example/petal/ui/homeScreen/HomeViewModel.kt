package com.example.petal.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.components.groupMemories
import com.example.petal.domain.Memory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomeViewModel(
    private val memoryRepository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    private var currentSearch: String? = null
    private var currentFilter: HomeFilter = HomeFilter.ALL

    val uiState: StateFlow<HomeUiState> = _uiState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun clearError() { _errorMessage.value = null }

    init { loadMemories() }

    fun onSearchChange(search: String) {
        currentSearch = search
        loadMemories()
    }

    fun onFilterChange(filter: HomeFilter) {
        currentFilter = filter
        loadMemories()
    }

    fun loadMemories() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            var attempts = 0
            val maxAttempts = 3

            while (attempts < maxAttempts) {
                try {
                    val memories = memoryRepository.getMemories(
                        search = currentSearch,
                        filter = currentFilter
                    )
                    _uiState.value = HomeUiState.Success(memories)
                    return@launch  // success, stop retrying
                } catch (e: SocketTimeoutException) {
                    attempts++
                    if (attempts < maxAttempts) {
                        // stay in Loading state with a connecting message
                        _uiState.value = HomeUiState.Connecting(attempts)
                        delay(3000)
                    } else {
                        _uiState.value = HomeUiState.Error("Server is waking up. Please try again in a moment.")
                    }
                } catch (e: Exception) {
                    _uiState.value = HomeUiState.Error(e.message ?: "Unable to load memories")
                    return@launch
                }
            }
        }
    }

    fun favoriteById(memoryId: Int) {
        viewModelScope.launch {
            try {
                memoryRepository.favorite(memoryId)
                loadMemories()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unable to update favorite"
            }
        }
    }

    fun refresh() { loadMemories() }
}