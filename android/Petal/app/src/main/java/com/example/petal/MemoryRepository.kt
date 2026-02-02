package com.example.petal

import android.content.Context
import android.net.Uri
import com.example.petal.data.remote.MemoryApi
import com.example.petal.domain.Memory
import com.example.petal.ui.editMemory.EditMemoryReq
import com.example.petal.ui.homeScreen.HomeFilter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.collections.mapIndexed

class MemoryRepository(
    private val memoryApi: MemoryApi
) {
    suspend fun getMemories(
        search: String? = null,
        filter: HomeFilter
    ): List<Memory> {
        val response = memoryApi.getMemories(
            search = search,
            tags = filter.tag,
            mood = filter.mood,
            isFavorite = filter.isFavorite
        )

        return response.results.map(MemoryMapper::map)
    }


    suspend fun getMemory(id: Int): Memory {
        return MemoryMapper.map(memoryApi.getMemory(id))
    }
    suspend fun favorite(id: Int) {
        memoryApi.favorite(id)
    }
    suspend fun unfavorite(id: Int) {
        memoryApi.unfavorite(id)
    }
    suspend fun createMemory(memory: Memory): Memory {
        val response = memoryApi.createMemoryJson(
            MemoryApi.CreateMemoryReq(
                title = memory.title,
                note = memory.note,
                mood = memory.mood?.value,
                tags = memory.tags,
                latitude = memory.location?.latitude,
                longitude = memory.location?.longitude
            )
        )
        return MemoryMapper.map(response)
    }


    suspend fun uploadMemoryImages(
        context: Context,
        memoryId: Int,
        imageUris: List<Uri>
    ) {
        if (imageUris.isEmpty()) return

        val parts = imageUris.mapIndexed { index, uri ->
            val file = uri.toFile(context)
            MultipartBody.Part.createFormData(
                name = "image_files",
                filename = "image_${index}_${System.currentTimeMillis()}.jpg",
                body = file.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        memoryApi.uploadMemoryImages(
            id = memoryId,
            images = parts
        )
    }

    suspend fun deleteMemory(id: Int) {
        memoryApi.deleteMemory(id)
    }
    suspend fun updateMemory(id: Int, memory: Memory){
        memoryApi.updateMemory(
            id= id,
            body = EditMemoryReq(
                title = memory.title,
                note = memory.note,
                mood = memory.mood?.value,
                tags = memory.tags,
                latitude = memory.location?.latitude,
                longitude = memory.location?.longitude
            )
        )

    }


}