package com.example.petal.ui.mapScreen

import com.example.petal.domain.Memory

data class LocationPin(
    val latitude: Double,
    val longitude: Double,
    val name: String?,               // ← add this field if not already present
    val memories: List<Memory> = emptyList()
)
