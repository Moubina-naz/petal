package com.example.petal.ui.editMemory

import com.example.petal.domain.MemoryImage
import com.example.petal.domain.Mood

data class EditMemoryUiState(
    val title: String = "",
    val note: String = "",
    val mood: Mood? = null,
    val tags: List<String> = emptyList(),

    val latitude: Double? = null,
    val longitude: Double? = null,

    val dateLabel: String = "",
    val locationLabel: String = "",

    val images: List<EditableImage> = emptyList(),
    val isSaving: Boolean = false
)

