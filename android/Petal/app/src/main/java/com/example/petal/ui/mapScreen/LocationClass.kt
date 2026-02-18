package com.example.petal.ui.mapScreen

sealed class LocationSource {

    data class Selected(
        val latitude: Double,
        val longitude: Double,
        val name: String?
    ) : LocationSource()

    data object None : LocationSource()

    data object Current : LocationSource()
}

