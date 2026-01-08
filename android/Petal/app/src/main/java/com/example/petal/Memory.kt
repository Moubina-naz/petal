package com.example.petal

import android.os.Build
import java.time.Instant


data class Memory (
    val id: Int? = null,                 // null when it's a new memory not saved yet
    val serverId: Int? = null,           // the ID from Django after syncing

    val title: String,
    val note: String,

    val location: Location? = null,      // uses the Location class we made earlier

    val audioUrl: String? = null,
    val musicUrl: String? = null,

    val tags: List<String> = emptyList(),

    val mood: Mood? = null,              // uses the Mood enum we made

    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,

    val revision: Int = 1,

    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),

    val images: List<MemoryImage> = emptyList()
)




