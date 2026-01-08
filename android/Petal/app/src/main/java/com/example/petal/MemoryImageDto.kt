package com.example.petal

import com.google.gson.annotations.SerializedName

data class MemoryImageDto(
    val id: Int,

    @SerializedName("memory")
    val memoryId: Int,

    val image: String,  // Raw URL from backend

    val caption: String?,

    val order: Int,

    @SerializedName("created_at")
    val createdAt: String
)