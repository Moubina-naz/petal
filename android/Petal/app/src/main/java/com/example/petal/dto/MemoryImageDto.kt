package com.example.petal.dto

import com.google.gson.annotations.SerializedName

data class MemoryImageDto(
    val id: Int,

    @SerializedName("image_url")
    val imageUrl: String?,

    val caption: String?,
    val order: Int,

    @SerializedName("created_at")
    val createdAt: String
)
