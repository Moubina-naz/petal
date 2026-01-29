package com.example.petal.dto

import com.example.petal.dto.MemoryImageDto
import com.google.gson.annotations.SerializedName

data class MemoryDto(
    val id: Int,

    val title: String,
    val note: String,

    val latitude: Double?,
    val longitude: Double?,

    val audio: String?,

    @SerializedName("music_url")
    val musicUrl: String?,

    val tags: List<String>,

    val mood: Int?,  // 1–10 (matches backend)

    @SerializedName("is_favorite")
    val isFavorite: Boolean,

    @SerializedName("is_deleted")
    val isDeleted: Boolean,

    val revision: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    val images: List<MemoryImageDto>
)