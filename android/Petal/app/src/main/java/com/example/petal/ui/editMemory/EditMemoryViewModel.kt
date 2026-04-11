package com.example.petal.ui.editMemory

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.components.AudioRecorder
import com.example.petal.domain.Location
import com.example.petal.domain.Memory
import com.example.petal.domain.MemoryImage
import com.example.petal.domain.Mood
import com.example.petal.ui.mapScreen.LocationSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
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

    private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    private val _uiState = MutableStateFlow(EditMemoryUiState())
    val uiState: StateFlow<EditMemoryUiState> = _uiState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun clearError() {
        _errorMessage.value = null
    }

    private var memoryId: String = ""

    fun initialize(memoryId: String, locationSource: LocationSource = LocationSource.None) {
        this.memoryId = memoryId
        loadMemory(locationSource)
    }

    private fun loadMemory(locationSource: LocationSource) {
        viewModelScope.launch {
            try {
                val id = memoryId.toIntOrNull() ?: return@launch
                val memory = repository.getMemory(id)

                val instant = memory.memoryDateTime ?: memory.createdAt
                val zoned = instant.atZone(ZoneId.systemDefault())

                val baseState = EditMemoryUiState(
                    title = memory.title,
                    note = memory.note,
                    mood = memory.mood,
                    tags = memory.tags,
                    locationName = memory.location?.name ?: "",
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
                    },
                    existingAudioUrl = memory.audioUrl
                )

                _uiState.value = when (locationSource) {
                    is LocationSource.Selected -> baseState.copy(
                        latitude = locationSource.latitude,
                        longitude = locationSource.longitude,
                        locationName = locationSource.name ?: ""
                    )

                    else -> baseState
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load memory"
            }
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

    fun onMoodSelected(mood: Mood) {
        _uiState.update { it.copy(mood = mood) }
    }

    fun addImage(uri: String) {
        _uiState.update {
            it.copy(images = it.images + EditableImage(localUri = uri, order = it.images.size))
        }
    }

    fun removeImage(image: EditableImage) {
        _uiState.update {
            it.copy(images = it.images.filterNot { it.localUri == image.localUri })
        }
    }

    fun removeImageById(imageUri: String) {
        _uiState.value.images.find { it.localUri == imageUri }?.let { removeImage(it) }
    }

    fun removeImageByIndex(index: Int) {
        if (index in _uiState.value.images.indices) removeImage(_uiState.value.images[index])
    }

    fun setLocation(latitude: Double, longitude: Double, name: String) {
        _uiState.update { it.copy(latitude = latitude, longitude = longitude, locationName = name) }
    }

    fun clearLocation() {
        _uiState.update { it.copy(latitude = null, longitude = null, locationName = "") }
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val state = _uiState.value
                val id = memoryId.toInt()

                val memoryInstant = LocalDateTime.of(state.selectedDate, state.selectedTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()

                val location = if (state.latitude != null && state.longitude != null) {
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

                val newImages = state.images
                    .map { it.localUri }
                    .filter { it.startsWith("content://") }

                repository.uploadMemoryImages(
                    context = context,
                    memoryId = id,
                    imageUris = newImages.map { Uri.parse(it) }
                )

                onDone()

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Save failed"
                _uiState.update { it.copy(error = e.message ?: "Save failed") }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    private val audioRecorder = AudioRecorder(context)

    var recordedAudioFile by mutableStateOf<File?>(null)
        private set
    var isRecording by mutableStateOf(false)
        private set

    fun startRecording() {
        isRecording = true; audioRecorder.start()
    }

    fun stopRecording() {
        isRecording = false; recordedAudioFile = audioRecorder.stop()
    }

    fun deleteNewAudio() {
        audioRecorder.delete(); recordedAudioFile = null
    }

    fun deleteExistingAudio() {
        _uiState.update { it.copy(existingAudioUrl = null, audioDeleted = true) }
    }
}