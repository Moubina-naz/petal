package com.example.petal.domain

data class Location(
    val latitude: Double,
    val longitude: Double,
    val name: String? = "Unknown location"
)