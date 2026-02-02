package com.example.petal.ui.addMemory

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.domain.Memory
import com.example.petal.domain.Mood
import com.example.petal.ui.editMemory.EditableImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddMemoryViewModel(
    private val repository: MemoryRepository,
    private val context: Context
) : ViewModel() {
    private var memoryId: String = ""
    private val _uiState = MutableStateFlow(AddMemoryUiState())
    val uiState: StateFlow<AddMemoryUiState> = _uiState

    init {


        val now = Instant.now()
        val zone = ZoneId.systemDefault()

        _uiState.value = AddMemoryUiState(
            dateLabel = now.atZone(zone).toLocalDate()
                .format(DateTimeFormatter.ofPattern("MMMM dd")),
            timeLabel = now.atZone(zone).toLocalTime()
                .format(DateTimeFormatter.ofPattern("hh:mm a"))
        )
    }
    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onNoteChange(value: String) {
        _uiState.update { it.copy(note = value) }
    }

    fun onMoodClick() {

        _uiState.update { it.copy(mood = Mood.HAPPY) }
    }

    fun onLocationChange(value: String) {
        _uiState.update { it.copy(location = value) }
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

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val state = _uiState.value

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

                val created = repository.createMemory(memory)

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

