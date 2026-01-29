package com.example.petal.dto

import com.google.gson.annotations.SerializedName

data class MemoryImageDto(
    val id: Int,
    val image: String,
    val caption: String?,
    val order: Int,

    @SerializedName("created_at")
    val createdAt: String
)