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

class MapViewModel (
    private val repository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState

    private val _selectedLocation = MutableStateFlow<LocationPin?>(null)
    val selectedLocation: StateFlow<LocationPin?> = _selectedLocation
    private val _selectedPin = MutableStateFlow<LocationPin?>(null)

    val selectedPin: StateFlow<LocationPin?> = _selectedPin

    init {
        loadMemories()
    }

    fun loadMemories() {
        viewModelScope.launch {
            try {
                val memories = repository.getMemories(
                    search = null,
                    filter = HomeFilter.ALL
                )

                val locatedMemories = memories.filter {
                    it.location != null
                }

                _uiState.value = MapUiState.Success(locatedMemories)
            } catch (e: Exception) {
                _uiState.value = MapUiState.Error(
                    e.message ?: "Failed to load map"
                )
            }
        }
    }

    fun onMapClick(lat: Double, lng: Double, name: String?) {
        _selectedLocation.value = LocationPin(
            latitude = lat,
            longitude = lng,
            name = name,
            memories = emptyList()
        )
    }

    fun onPinClick(pin: LocationPin) {
        _selectedLocation.value = pin   // already has memories
    }

    fun onMarkerClick(memory: Memory) {
        _selectedLocation.value = LocationPin(
            memory.location!!.latitude,
            memory.location.longitude,
            memory.title
        )
    }

    fun clearSelection() {
        _selectedLocation.value = null
    }

    fun groupedMemories(): Map<String, List<Memory>> {
        val state = _uiState.value
        if (state !is MapUiState.Success) return emptyMap()

        return state.memories.groupBy { memory ->
            val loc = memory.location!!
            "${loc.latitude},${loc.longitude}"
        }
    }


    fun clearPin() {
        _selectedPin.value = null
    }
    private val _searchResults = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val searchResults: StateFlow<List<AutocompletePrediction>> = _searchResults

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun onSearchQueryChange(query: String, placesClient: PlacesClient?) {
        _searchQuery.value = query  // ← moved to top

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
            .addOnFailureListener {
                _searchResults.value = emptyList()
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

                // Use a meaningful location name — prefer the stored one if available
                val locationName = first.location?.name
                    ?: "Unknown place"

                LocationPin(
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    name = locationName,
                    memories = memoriesAtLoc
                )
            }
    }

}
