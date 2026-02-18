package com.example.petal.ui.editMemory

import com.google.gson.annotations.SerializedName

data class EditMemoryReq(
val title: String,
val note: String,
val mood: Int?,
val tags: List<String>,
val latitude: Double?,
val longitude: Double?,

@SerializedName("location_name")
val locationName: String?,

@SerializedName("memory_datetime")
val memoryDateTime: String?
)