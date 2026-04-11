package com.example.petal.ui.editMemory

import com.example.petal.domain.MemoryImage
import com.example.petal.domain.Mood
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class EditMemoryUiState(
    val title: String = "",
    val note: String = "",
    val mood: Mood? = null,
    val tags: List<String> = emptyList(),

    val locationName: String = "Unknown location",
    val latitude: Double? = null,
    val longitude: Double? = null,

    val selectedDate: LocalDate = LocalDate.now(),
    val selectedTime: LocalTime = LocalTime.now(),

    val images: List<EditableImage> = emptyList(),

    val isSaving: Boolean = false,
    val error: String? = null,
    val existingAudioUrl: String? = null,
    val audioDeleted: Boolean = false
) {
    val dateLabel: String
        get() = selectedDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))

    val timeLabel: String
        get() = selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
}


