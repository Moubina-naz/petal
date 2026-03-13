package com.example.petal.ui.profile

import com.example.petal.domain.Mood

data class ProfileUiState(
    val username: String = "",
    val totalMemories: Int = 0,
    val totalPhotos: Int = 0,
    val totalVoice: Int = 0,
    val dominantMood: Mood? = null,
    val streak: Int = 0,
    val isLoading: Boolean = true
)