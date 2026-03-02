package com.example.petal.ui.profile

data class ProfileUiState(
    val username: String = "",
    val totalMemories: Int = 0,
    val totalPhotos: Int = 0,
    val totalVoice: Int = 0,
    val dominantMood: String = "None",
    val streak: Int = 0,
    val isLoading: Boolean = true
)