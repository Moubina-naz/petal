package com.example.petal.ui.mapScreen

import java.io.Serializable

sealed class LocationSource : Serializable {

    data class Selected(
        val latitude: Double,
        val longitude: Double,
        val name: String?
    ) : LocationSource()

    data object None : LocationSource()

    data object Current : LocationSource()
}

