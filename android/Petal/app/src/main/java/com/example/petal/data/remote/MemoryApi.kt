package com.example.petal.data.remote

import com.example.petal.domain.PaginatedResponse
import com.example.petal.ui.editMemory.EditMemoryReq
import com.example.petal.dto.MemoryDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface MemoryApi {

    @GET("memories/")
    suspend fun getMemories(
        @Header("Authorization")
        token: String = "Bearer ${AuthToken.TOKEN}",
        @Query("search") search: String? = null,
        @Query("tags") tags: String? = null,
        @Query("mood") mood: Int? = null,
        @Query("is_favorite") isFavorite: Boolean? = null,
    ): PaginatedResponse<MemoryDto>

    @GET("memories/{id}/")
    suspend fun getMemory(
        @Path("id") id: Int,
        @Header("Authorization")
        token: String = "Bearer ${AuthToken.TOKEN}"

    ): MemoryDto

    @PATCH("memories/{id}/favorite/")
    suspend fun favorite(
        @Path("id") id: Int,
        @Header("Authorization")
        token: String = "Bearer ${AuthToken.TOKEN}"
    )

    @PATCH("memories/{id}/unfavorite/")
    suspend fun unfavorite(
        @Path("id") id: Int,
        @Header("Authorization")
        token: String = "Bearer ${AuthToken.TOKEN}"
    )
    @DELETE("memories/{id}/")
    suspend fun deleteMemory(
        @Path("id") id: Int,
        @Header("Authorization")
        token: String = "Bearer ${AuthToken.TOKEN}"
    )
    @PUT("memories/{id}/")
    suspend fun updateMemory(
        @Path("id") id: Int,
        @Body body: EditMemoryReq,
        @Header ("Authorization")
        token: String = "Bearer ${AuthToken.TOKEN}"
    )

}