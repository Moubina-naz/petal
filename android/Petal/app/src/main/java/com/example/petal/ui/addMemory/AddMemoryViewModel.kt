package com.example.petal.ui.addMemory

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.domain.Location
import com.example.petal.domain.Memory
import com.example.petal.domain.Mood
import com.example.petal.ui.editMemory.EditableImage
import com.example.petal.ui.mapScreen.LocationSource
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

class AddMemoryViewModel(
    private val repository: MemoryRepository,
    private val context: Context,
    private val locationSource: LocationSource
) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location
    private var memoryId: String = ""
    private val _uiState = MutableStateFlow(AddMemoryUiState())
    val uiState: StateFlow<AddMemoryUiState> = _uiState

    private val dateFormatter =
        DateTimeFormatter.ofPattern("MMM d, yyyy")

    private val timeFormatter =
        DateTimeFormatter.ofPattern("hh:mm a")


    init {
        when (locationSource) {

            is LocationSource.Selected -> {
                _location.value = Location(
                    latitude = locationSource.latitude,
                    longitude = locationSource.longitude,
                    name = locationSource.name ?: "Unknown location"
                )
                _uiState.update {
                    it.copy(location = locationSource.name ?: "Unknown location")
                }
            }

            LocationSource.None -> {
                _location.value = null
                _uiState.update {
                    it.copy(location = "Unknown location")
                }
            }


            LocationSource.Current -> {
                _location.value = null
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update {
            it.copy(selectedDate = date)
        }
    }

    fun onTimeSelected(time: LocalTime) {
        _uiState.update {
            it.copy(selectedTime = time)
        }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onNoteChange(value: String) {
        _uiState.update { it.copy(note = value) }
    }

    fun onMoodSelected(mood: Mood) {
        _uiState.update {
            it.copy(mood = mood)
        }
    }

    fun removeImage(image: EditableImage) {
        _uiState.update { state ->
            state.copy(
                images = state.images.filterNot { it.localUri == image.localUri }
            )
        }
    }
    fun onImagesPicked(uris: List<Uri>) {
        _uiState.update { state ->
            val current = state.images
            val canAdd = 5 - current.size
            if (canAdd <= 0) return@update state

            val newImages = uris.take(canAdd).mapIndexed { idx, uri ->
                EditableImage(
                    localUri = uri.toString(),
                    order = current.size + idx
                )
            }

            state.copy(images = current + newImages)
        }
    }

    fun onLocationChange(value: String) {

        _uiState.update { it.copy(location = value) }
        _location.update { loc ->
            loc?.copy(name = value)
        }
    }


    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            println("=== SAVE DEBUG START ===")
            println("UI location text: '${uiState.value.location}'")                    // what the text field shows
            println("ViewModel _location.value?.name: '${_location.value?.name}'")      // what VM thinks the name is
            println("Location object before save: ${_location.value}")                   // full object

            _uiState.update { it.copy(isSaving = true, error = null) }
            val date = uiState.value.selectedDate
            val time = uiState.value.selectedTime
            val state = _uiState.value
            val memoryDateTime =
                LocalDateTime.of(date, time)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()

            try {

                if (state.title.isBlank()) {
                    _uiState.update {
                        it.copy(isSaving = false, error = "Title is required")
                    }
                    return@launch
                }

                val memory = Memory(
                    id = 0,
                    title = state.title,
                    note = state.note,
                    mood = state.mood,
                    tags = state.tags,
                    location = _location.value,
                    audioUrl = null,
                    musicUrl = null,
                    isFavorite = false,
                    isDeleted = false,
                    revision = 0,
                    memoryDateTime = memoryDateTime,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now(),
                    images = emptyList()
                )
                println("Memory location name right before repo: '${memory.location?.name}'")
                val created = repository.createMemory(memory)
                println("After creation - server returned location_name: '${created.location?.name}'")
                println("=== SAVE DEBUG END ===")
                repository.uploadMemoryImages(
                    context = context,
                    memoryId = created.id,
                    imageUris = state.images.map { Uri.parse(it.localUri) }
                )

                _uiState.update { it.copy(isSaving = false) }
                onDone()

            } catch (e: Exception) {
                if (e is retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("API_ERROR", "Code=${e.code()} body=$errorBody")
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = errorBody ?: "HTTP ${e.code()}"
                        )
                    }
                } else {
                    Log.e("API_ERROR", e.stackTraceToString())
                    _uiState.update {
                        it.copy(isSaving = false, error = e.message)
                    }
                }
            }

        }
        }
    }

