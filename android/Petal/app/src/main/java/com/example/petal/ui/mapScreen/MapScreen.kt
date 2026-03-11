package com.example.petal.ui.mapScreen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petal.NavigationEvent
import com.example.petal.data.remote.ApiProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.util.Locale
import android.location.Geocoder
import android.location.Address
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mode: MapMode,
    locationSource: LocationSource,
    onLocationPicked: (Double, Double, String?) -> Unit,
    onDismiss: () -> Unit
){
    val viewModel = remember { MapViewModel(ApiProvider.memoryRepository) }
    LaunchedEffect(locationSource) {
        if (locationSource is LocationSource.Selected) {
            viewModel.onMapClick(
                locationSource.latitude,
                locationSource.longitude,
                locationSource.name ?: "Selected Location"
            )
        }
    }
    val context = LocalContext.current

    val placesClient = remember {
        if (Places.isInitialized()) {
            android.util.Log.d("PLACES_INIT", "Client created OK")
            Places.createClient(context)
        } else {
            android.util.Log.e("PLACES_INIT", "NOT initialized - client is null")
            null
        }
    }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    )
    var isMapLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!locationPermissionsState.allPermissionsGranted) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(20.5937, 78.9629), 5f) // Central India
    }

    LaunchedEffect(selectedLocation, isMapLoaded) {
        val pin = selectedLocation ?: return@LaunchedEffect
        if (!isMapLoaded) return@LaunchedEffect

        val latLng = LatLng(pin.latitude, pin.longitude)

        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                if (pin.memories.isEmpty()) 12f else 10f
            )
        )
    }


    LaunchedEffect(locationPermissionsState.allPermissionsGranted, isMapLoaded) {
        if (
            isMapLoaded &&
            selectedLocation == null &&
            locationPermissionsState.allPermissionsGranted
        ) {
            delay(300L)  // small delay to let LaunchedEffect(locationSource) in MapVoyagerScreen run

            if (selectedLocation == null) {  // re-check after delay
                getCurrentLocation(fusedLocationClient, context) { latLng ->
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        val name = getPlaceNameFromLatLng(latLng, context)
                        viewModel.onMapClick(latLng.latitude, latLng.longitude, name)
                    }
                }
            }
        }
    }
    val focusRequester = remember { FocusRequester() }

// Add this LaunchedEffect
    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            focusRequester.requestFocus()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is MapUiState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            is MapUiState.Error -> {
                Text(
                    text = (uiState as MapUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is MapUiState.Success -> {
                val pins = remember(uiState) { viewModel.getPins() }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapLoaded = {
                        isMapLoaded = true
                    },
                    onMapClick = { latLng ->
                        coroutineScope.launch {
                            val name = reverseGeocode(latLng, context)
                            viewModel.onMapClick(latLng.latitude, latLng.longitude, name)
                        }
                    },
                    onPOIClick = { poi ->
                        val latLng = poi.latLng
                        val name = poi.name

                        viewModel.onMapClick(
                            latLng.latitude,
                            latLng.longitude,
                            name
                        )
                    },
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = true
                    ),
                    properties = MapProperties(
                        isMyLocationEnabled = locationPermissionsState.allPermissionsGranted,
                        mapType = MapType.NORMAL
                    )
                ) {
                    pins.forEach { pin ->
                        Marker(
                            state = rememberMarkerState(position = LatLng(pin.latitude, pin.longitude)),
                            title = pin.memories.firstOrNull()?.title ?: "Location",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                            onClick = {
                                viewModel.onPinClick(pin)
                                true
                            }
                        )
                    }

                    selectedLocation?.let { selectedPin ->
                        Marker(
                            state = rememberMarkerState(position = LatLng(selectedPin.latitude, selectedPin.longitude)),
                            title = selectedPin.name ?: "Selected Location",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .zIndex(1f)

        ) {
            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it, placesClient) },
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                    placeholder = { Text("Search places...") },
                    leadingIcon = {
                        IconButton(onClick = {
                            isSearchActive = false
                            viewModel.clearSearch()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.onSearchQueryChange("", placesClient) }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            val top = searchResults.firstOrNull()
                            if (top != null) {
                                val placeFields = listOf(
                                    com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
                                    com.google.android.libraries.places.api.model.Place.Field.NAME,
                                    com.google.android.libraries.places.api.model.Place.Field.ADDRESS
                                )
                                val fetchRequest = com.google.android.libraries.places.api.net.FetchPlaceRequest
                                    .newInstance(top.placeId, placeFields)
                                placesClient?.fetchPlace(fetchRequest)
                                    ?.addOnSuccessListener { response ->
                                        val place = response.place
                                        val latLng = place.latLng
                                        if (latLng != null) {
                                            coroutineScope.launch {
                                                cameraPositionState.animate(
                                                    CameraUpdateFactory.newLatLngZoom(latLng, 14f)
                                                )
                                                viewModel.onMapClick(latLng.latitude, latLng.longitude, place.name ?: place.address)
                                                isSearchActive = false
                                                viewModel.clearSearch()
                                            }
                                        }
                                    }
                            }
                        }
                    )



                    )

                if (searchResults.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column {
                            searchResults.take(5).forEach { prediction ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val placeFields = listOf(
                                                com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
                                                com.google.android.libraries.places.api.model.Place.Field.NAME,
                                                com.google.android.libraries.places.api.model.Place.Field.ADDRESS
                                            )
                                            val fetchRequest = com.google.android.libraries.places.api.net.FetchPlaceRequest
                                                .newInstance(prediction.placeId, placeFields)

                                            placesClient?.fetchPlace(fetchRequest)
                                                ?.addOnSuccessListener { response ->
                                                    val place = response.place
                                                    val latLng = place.latLng
                                                    if (latLng != null) {
                                                        coroutineScope.launch {
                                                            cameraPositionState.animate(
                                                                CameraUpdateFactory.newLatLngZoom(latLng, 14f)
                                                            )
                                                            viewModel.onMapClick(
                                                                latLng.latitude,
                                                                latLng.longitude,
                                                                place.name ?: place.address
                                                            )
                                                            isSearchActive = false
                                                            viewModel.clearSearch()
                                                        }
                                                    }
                                                }
                                        }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF9E6F73),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = prediction.getPrimaryText(null).toString(),
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = prediction.getSecondaryText(null).toString(),
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                                if (prediction != searchResults.last()) HorizontalDivider()
                            }
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Petal", style = MaterialTheme.typography.titleLarge, color = Color(0xFF3E2F26))
                    IconButton(onClick = { isSearchActive = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            }
        }
    }

    selectedLocation?.let { pin ->
        ModalBottomSheet(
            onDismissRequest = { viewModel.clearSelection() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.LightGray, RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.height(20.dp))

                Text(
                    text = pin.name ?: "Selected Location",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    "Lat: ${"%.6f".format(pin.latitude)}, Lon: ${"%.6f".format(pin.longitude)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(24.dp))

                if (mode == MapMode.CREATE_MEMORY) {

                    Button(
                        onClick = {
                            onLocationPicked(pin.latitude, pin.longitude, pin.name)
                            viewModel.clearSelection()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Add Memory")
                    }

                } else {

                    Button(
                        onClick = {
                            onLocationPicked(pin.latitude, pin.longitude, pin.name)
                            viewModel.clearSelection()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Location")
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            viewModel.clearSelection()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Dismiss")
                    }
                }

                Spacer(Modifier.height(32.dp))

                Text("MEMORIES NEARBY", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(12.dp))

                if (pin.memories.isEmpty()) {
                    Text("(No memories yet)", color = Color.Gray)
                } else {
                    pin.memories.forEach { memory ->
                        ListItem(
                            headlineContent = {
                                Text(pin.name ?: "Unknown Location")
                            },
                            supportingContent = {

                                Text(
                                    memory.createdAt
                                        .atZone(java.time.ZoneId.systemDefault())
                                        .format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy • HH:mm"))
                                )
                            },
                            leadingContent = { Text("📅") },
                            modifier = Modifier.clickable {
                                // TODO: navigate to detail
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
private suspend fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: android.content.Context,
    onLocationReceived: (LatLng) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onLocationReceived(LatLng(it.latitude, it.longitude))
        }
    }
}

private fun getPlaceNameFromLatLng(latLng: LatLng, context: android.content.Context): String? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        addresses?.firstOrNull()?.let { addr ->
            addr.getAddressLine(0)
                ?: addr.featureName
                ?: addr.locality
                ?: addr.subLocality
                ?: null
        }
    } catch (e: Exception) {
        null  // silent fail → name will be null
    }
}private suspend fun reverseGeocode(latLng: LatLng, context: android.content.Context): String? {

    return try {
        val geocoder = android.location.Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        addresses?.firstOrNull()?.let { address ->
            "${address.featureName ?: ""}, ${address.locality ?: ""}".trim().takeIf { it.isNotEmpty() }
        }
    } catch (e: Exception) {
        null
    }
}