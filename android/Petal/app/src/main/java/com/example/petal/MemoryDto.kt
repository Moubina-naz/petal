package com.example.petal

import com.google.gson.annotations.SerializedName

data class MemoryDto(
    val id: Int,

    @SerializedName("user")
    val userId: Int,  // Just the ID, no full user object needed yet

    val title: String,
    val note: String,

    val latitude: Double?,
    val longitude: Double?,

    val audio: String?,  // Backend URL or empty
    @SerializedName("music_url")
    val musicUrl: String?,

    val tags: List<String>,

    val mood: Int?,  // Raw number 1-5

    @SerializedName("is_favorite")
    val isFavorite: Boolean,

    @SerializedName("is_deleted")
    val isDeleted: Boolean,

    val revision: Int,

    @SerializedName("created_at")
    val createdAt: String,  // Raw ISO string like "2023-10-24T12:00:00Z"

    @SerializedName("updated_at")
    val updatedAt: String
)
