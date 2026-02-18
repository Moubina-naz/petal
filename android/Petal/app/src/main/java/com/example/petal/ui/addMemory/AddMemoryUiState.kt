package com.example.petal.ui.addMemory

import com.example.petal.domain.Mood
import com.example.petal.ui.editMemory.EditableImage
import java.time.LocalDate
import java.time.LocalTime

data class AddMemoryUiState(
    val title: String = "",
    val note: String = "",
    val mood: Mood? = null,
    val tags: List<String> = emptyList(),
    val location: String = "",
    val images: List<EditableImage> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedTime: LocalTime = LocalTime.now(),

    val isSaving: Boolean = false,
    val error: String? = null
)
