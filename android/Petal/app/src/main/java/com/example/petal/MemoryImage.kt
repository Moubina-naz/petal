package com.example.petal

import java.time.Instant

data class MemoryImage(
    val id: Int? = null,                 // null when the image is new and not yet synced
    val memoryId: Int,                   // which memory this image belongs to
    val imageUrl: String,                // can be a remote URL or local file path
    val caption: String? = null,
    val order: Int = 0,
    val createdAt: Instant = Instant.now()
)