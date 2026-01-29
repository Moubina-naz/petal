package com.example.petal.ui.editMemory

data class EditMemoryReq(
val title: String,
val note: String,
val mood: Int?,
val tags: List<String>,
val latitude: Double?,
val longitude: Double?
)