package com.example.petal.ui.mapScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petal.MemoryRepository
import com.example.petal.domain.Memory
import com.example.petal.ui.homeScreen.HomeFilter
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState

    private val _selectedLocation = MutableStateFlow<LocationPin?>(null)
    val selectedLocation: StateFlow<LocationPin?> = _selectedLocation

    private val _selectedPin = MutableStateFlow<LocationPin?>(null)
    val selectedPin: StateFlow<LocationPin?> = _selectedPin

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun clearError() { _errorMessage.value = null }

    private val _searchResults = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val searchResults: StateFlow<List<AutocompletePrediction>> = _searchResults

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init { loadMemories() }

    fun loadMemories() {
        viewModelScope.launch {
            try {
                val memories = repository.getMemories(search = null, filter = HomeFilter.ALL)
                _uiState.value = MapUiState.Success(memories.filter { it.location != null })
            } catch (e: Exception) {
                _uiState.value = MapUiState.Error(e.message ?: "Failed to load map")
            }
        }
    }

    fun onMapClick(lat: Double, lng: Double, name: String?) {
        _selectedLocation.value = LocationPin(
            latitude  = lat,
            longitude = lng,
            name      = name,
            memories  = emptyList()
        )
    }

    fun onPinClick(pin: LocationPin) { _selectedLocation.value = pin }

    fun onMarkerClick(memory: Memory) {
        _selectedLocation.value = LocationPin(
            memory.location!!.latitude,
            memory.location.longitude,
            memory.title
        )
    }

    fun clearSelection() { _selectedLocation.value = null }
    fun clearPin() { _selectedPin.value = null }

    fun groupedMemories(): Map<String, List<Memory>> {
        val state = _uiState.value
        if (state !is MapUiState.Success) return emptyMap()
        return state.memories.groupBy { "${it.location!!.latitude},${it.location!!.longitude}" }
    }

    fun onSearchQueryChange(query: String, placesClient: PlacesClient?) {
        _searchQuery.value = query

        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        if (placesClient == null) return

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                _searchResults.value = response.autocompletePredictions
            }
            .addOnFailureListener { e ->
                _searchResults.value = emptyList()
                _errorMessage.value = e.message ?: "Search failed"
            }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun getPins(): List<LocationPin> {
        val state = uiState.value
        if (state !is MapUiState.Success) return emptyList()

        return state.memories
            .filter { it.location != null }
            .groupBy { "${it.location!!.latitude},${it.location!!.longitude}" }
            .map { (_, memoriesAtLoc) ->
                val first = memoriesAtLoc.first()
                val loc = first.location!!
                LocationPin(
                    latitude  = loc.latitude,
                    longitude = loc.longitude,
                    name      = first.location?.name ?: "Unknown place",
                    memories  = memoriesAtLoc
                )
            }
    }
}
