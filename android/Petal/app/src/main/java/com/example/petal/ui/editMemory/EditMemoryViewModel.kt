package com.example.petal.ui.editMemory

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.domain.Location
import com.example.petal.domain.Memory
import com.example.petal.domain.MemoryImage
import com.example.petal.domain.Mood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class EditMemoryViewModel(
    private val repository: MemoryRepository,
    private val context: Context

) : ViewModel() {

    private val _uiState = MutableStateFlow(EditMemoryUiState())
    private var memoryId: String = ""
    val uiState: StateFlow<EditMemoryUiState> = _uiState

    fun initialize(memoryId: String) {
        this.memoryId = memoryId
        loadMemory()
    }


    private fun loadMemory() {
        viewModelScope.launch {

            val id = memoryId.toIntOrNull() ?: return@launch
            val memory = repository.getMemory(id)

            val instant = memory.memoryDateTime ?: memory.createdAt

            val zoned = instant.atZone(ZoneId.systemDefault())

            _uiState.value = EditMemoryUiState(
                title = memory.title,
                note = memory.note,
                mood = memory.mood,
                tags = memory.tags,

                locationName = memory.location?.name ?: "Unknown location",
                latitude = memory.location?.latitude,
                longitude = memory.location?.longitude,

                selectedDate = zoned.toLocalDate(),
                selectedTime = zoned.toLocalTime(),

                images = memory.images.mapIndexed { index, img ->
                    EditableImage(
                        localUri = img.imageUrl,
                        caption = img.caption,
                        order = index
                    )
                }
            )
        }
    }
    fun onLocationNameChange(value: String) {
        _uiState.update { it.copy(locationName = value) }
    }
    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun onTimeSelected(time: LocalTime) {
        _uiState.update { it.copy(selectedTime = time) }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onNoteChange(value: String) {
        _uiState.update { it.copy(note = value) }
    }

    fun onMoodChange(value: Mood?) {
        _uiState.update { it.copy(mood = value) }
    }

    fun addImage(uri: String) {
        _uiState.update {
            it.copy(
                images = it.images + EditableImage(
                    localUri = uri,
                    order = it.images.size
                )
            )
        }
    }

    fun removeImage(image: EditableImage) {
        _uiState.update {
            it.copy(
                images = it.images.filterNot { it.localUri == image.localUri }
            )
        }
    }

    fun removeImageById(imageUri: String) {
        val imageToRemove = _uiState.value.images.find { it.localUri == imageUri }
        imageToRemove?.let { removeImage(it) }
    }

    fun removeImageByIndex(index: Int) {
        if (index in _uiState.value.images.indices) {
            removeImage(_uiState.value.images[index])
        }
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {

            _uiState.update { it.copy(isSaving = true) }

            val state = _uiState.value
            val id = memoryId.toIntOrNull() ?: return@launch

            // ✅ Build memory datetime
            val memoryInstant =
                LocalDateTime.of(state.selectedDate, state.selectedTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()

            // ✅ Build location object
            val location =
                if (state.latitude != null && state.longitude != null) {
                    Location(
                        latitude = state.latitude,
                        longitude = state.longitude,
                        name = state.locationName
                    )
                } else null

            repository.updateMemory(
                id = id,
                title = state.title,
                note = state.note,
                mood = state.mood,
                tags = state.tags,
                location = location,
                memoryDateTime = memoryInstant
            )

            // Upload new images
            val newImages = state.images
                .map { it.localUri }
                .filter { it.startsWith("content://") }
                .map { Uri.parse(it) }

            if (newImages.isNotEmpty()) {
                repository.uploadMemoryImages(
                    context = context,
                    memoryId = id,
                    imageUris = newImages
                )
            }

            _uiState.update { it.copy(isSaving = false) }
            onDone()
        }
    }


}
