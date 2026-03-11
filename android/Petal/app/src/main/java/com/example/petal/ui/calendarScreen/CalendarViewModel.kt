package com.example.petal.ui.calendarScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.domain.Memory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    private val repository: MemoryRepository
) : ViewModel() {

    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate

    private val _memoriesByDay = MutableStateFlow<Map<Int, List<Memory>>>(emptyMap())
    val memoriesByDay: StateFlow<Map<Int, List<Memory>>> = _memoriesByDay

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedDay = MutableStateFlow<Int?>(null)
    val selectedDay: StateFlow<Int?> = _selectedDay

    init {
        loadMonth()
    }

    fun goToPreviousMonth() {
        _currentDate.value = _currentDate.value.minusMonths(1)
        _selectedDay.value = null
        loadMonth()
    }

    fun goToNextMonth() {
        _currentDate.value = _currentDate.value.plusMonths(1)
        _selectedDay.value = null
        loadMonth()
    }

    fun selectDay(day: Int) {
        _selectedDay.value = day
    }

    private fun loadMonth() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val date = _currentDate.value
                _memoriesByDay.value = repository.getMemoriesByMonth(date.year, date.monthValue)
            } catch (e: Exception) {
                _memoriesByDay.value = emptyMap()
            } finally {
                _isLoading.value = false
            }
        }
    }
}