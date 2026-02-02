package com.example.petal.ui.addMemory

import com.example.petal.domain.Mood
import com.example.petal.ui.editMemory.EditableImage

data class AddMemoryUiState(
    val title: String = "",
    val note: String = "",
    val mood: Mood? = null,
    val tags: List<String> = emptyList(),
    val location: String = "",
    val images: List<EditableImage> = emptyList(),
    val dateLabel: String = "",
    val timeLabel: String = "",
    val isSaving: Boolean = false,
    val error: String? = null
)
