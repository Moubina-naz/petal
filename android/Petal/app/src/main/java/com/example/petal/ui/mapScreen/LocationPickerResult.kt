package com.example.petal.ui.mapScreen

import kotlinx.coroutines.flow.MutableStateFlow

object LocationPickerResult {
    val pickedLocation = MutableStateFlow<LocationSource.Selected?>(null)
}