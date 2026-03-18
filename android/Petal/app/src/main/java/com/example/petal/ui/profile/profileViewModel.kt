package com.example.petal.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.domain.Memory
import com.example.petal.domain.Mood
import com.example.petal.ui.auth.TokenManager
import com.example.petal.ui.homeScreen.HomeFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class ProfileViewModel(
    private val repository: MemoryRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun clearError() { _errorMessage.value = null }

    init { loadProfileData() }

    fun refresh() { loadProfileData() }

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                val profile = try {
                    repository.getProfile()
                } catch (e: Exception) {
                    null
                }

                val username = profile?.username
                    ?: tokenManager.getUsername()
                    ?: "User"

                val memories = repository.getMemories(
                    search = null,
                    filter = HomeFilter.ALL
                )

                _uiState.value = ProfileUiState(
                    username      = username,
                    totalMemories = memories.size,
                    totalPhotos   = memories.sumOf { it.images.size },
                    totalVoice    = memories.count { it.audioUrl != null },
                    dominantMood  = calculateDominantMood(memories),
                    streak        = calculateStreak(memories),
                    isLoading     = false
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load profile"
            }
        }
    }

    private fun calculateDominantMood(memories: List<Memory>): Mood? {
        val moodCounts = mutableMapOf<Mood, Int>()
        memories.forEach { memory ->
            memory.mood?.let {
                moodCounts[it] = moodCounts.getOrDefault(it, 0) + 1
            }
        }
        return moodCounts.maxByOrNull { it.value }?.key
    }

    private fun calculateStreak(memories: List<Memory>): Int {
        val dates = memories.mapNotNull { memory ->
            memory.memoryDateTime
                ?.atZone(ZoneId.systemDefault())
                ?.toLocalDate()
        }.distinct()

        if (dates.isEmpty()) return 0

        var streak = 0
        var current = LocalDate.now()
        while (dates.contains(current)) {
            streak++
            current = current.minusDays(1)
        }
        return streak
    }
}
