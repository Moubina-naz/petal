package com.example.petal

import com.example.petal.data.remote.MemoryApi
import com.example.petal.domain.Memory
import com.example.petal.ui.editMemory.EditMemoryReq
import com.example.petal.ui.homeScreen.HomeFilter

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