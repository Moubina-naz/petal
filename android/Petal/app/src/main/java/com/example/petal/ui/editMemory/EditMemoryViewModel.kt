package com.example.petal.ui.editMemory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.domain.Memory
import com.example.petal.domain.MemoryImage
import com.example.petal.domain.Mood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class EditMemoryViewModel(
    private val repository: MemoryRepository,

) : ViewModel() {

    private val _uiState = MutableStateFlow(EditMemoryUiState())
    private var memoryId: String = ""
    val uiState: StateFlow<EditMemoryUiState> = _uiState

    fun initialize(memoryId: String) {
        this.memoryId = memoryId
        if (memoryId.isNotEmpty()) {
            loadMemory()
        } else {
            // Initialize for NEW memory
            _uiState.value = EditMemoryUiState(
                dateLabel = Instant.now()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("MMMM dd")),
                locationLabel = "Location"
            )
        }
    }

    init {
        // Only load if memoryId is not empty (editing existing memory)
        if (memoryId.isNotEmpty()) {
            loadMemory()
        } else {
            // Initialize for NEW memory
            _uiState.value = EditMemoryUiState(
                dateLabel = Instant.now()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("MMMM dd")),
                locationLabel = "Location"
            )
        }
    }
    private fun loadMemory() {
        viewModelScope.launch {
            val memoryIdInt = memoryId.toIntOrNull()
            if (memoryIdInt != null) {
                val memory = repository.getMemory(memoryIdInt)

                _uiState.value = EditMemoryUiState(
                    title = memory.title,
                    note = memory.note,
                    mood = memory.mood,
                    tags = memory.tags,
                    dateLabel = memory.createdAt
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("MMMM dd")),
                    locationLabel = memory.location?.name ?: "Location",
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
            val memoryIdInt = memoryId.toIntOrNull()

            if (memoryIdInt != null) {
                // UPDATE existing memory
                repository.updateMemory(
                    id = memoryIdInt,
                    memory = Memory(
                        id = memoryIdInt,
                        title = state.title,
                        note = state.note,
                        mood = state.mood,
                        tags = state.tags,
                        location = null,
                        audioUrl = null,
                        musicUrl = null,
                        isFavorite = false,
                        isDeleted = false,
                        revision = 0,
                        createdAt = Instant.now(),
                        updatedAt = Instant.now(),
                        images = emptyList()
                    )
                )
            } else {
                // CREATE new memory

                _uiState.update { it.copy(isSaving = false) }
            }

            onDone()
        }
    }
}
