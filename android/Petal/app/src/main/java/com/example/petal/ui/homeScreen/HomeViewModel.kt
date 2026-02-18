package com.example.petal.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.ui.homeScreen.HomeFilter
import com.example.petal.MemoryRepository
import com.example.petal.components.groupMemories
import com.example.petal.domain.Memory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val memoryRepository: MemoryRepository
): ViewModel(){

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    private var currentSearch: String? = null
    private var currentFilter: HomeFilter = HomeFilter.ALL

    val uiState : StateFlow<HomeUiState> = _uiState
    init{
        loadMemories()
    }

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
            try{
                val memories = memoryRepository.getMemories(
                    search = currentSearch,
                    filter = currentFilter
                )
                val sections = groupMemories(memories)
                _uiState.value = HomeUiState.Success(memories)

            }catch( e: Exception) {
                _uiState.value = HomeUiState.Error(
                    e.message ?: "Unable to load memories"
                )
        }
        }
    }

    fun favoriteById(memoryId: Int) {
        viewModelScope.launch {
            try {
                memoryRepository.favorite(memoryId)
                loadMemories()
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(
                    e.message ?: "Unable to update favorite"
                )
            }
        }
    }

    fun refresh(){
            loadMemories()
        }
        }

