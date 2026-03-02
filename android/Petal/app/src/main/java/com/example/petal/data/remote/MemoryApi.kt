package com.example.petal.data.remote

import com.example.petal.domain.PaginatedResponse
import com.example.petal.ui.editMemory.EditMemoryReq
import com.example.petal.dto.MemoryDto
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface MemoryApi {

    @GET("memories/")
    suspend fun getMemories(
        @Query("search") search: String? = null,
        @Query("tags") tags: String? = null,
        @Query("mood") mood: Int? = null,
        @Query("is_favorite") isFavorite: Boolean? = null
    ): PaginatedResponse<MemoryDto>

    @GET("memories/{id}/")
    suspend fun getMemory(@Path("id") id: Int): MemoryDto

    @PATCH("memories/{id}/favorite/")
    suspend fun favorite(@Path("id") id: Int)

    @PATCH("memories/{id}/unfavorite/")
    suspend fun unfavorite(@Path("id") id: Int)

    @DELETE("memories/{id}/")
    suspend fun deleteMemory(@Path("id") id: Int)

    @PUT("memories/{id}/")
    suspend fun updateMemory(
        @Path("id") id: Int,
        @Body body: EditMemoryReq
    )

    @POST("memories/")
    suspend fun createMemoryJson(
        @Body body: MemoryApi.CreateMemoryReq   // ← use the inner class
    ): MemoryDto

    @Multipart
    @POST("memories/{id}/images/")
    suspend fun uploadMemoryImages(
        @Path("id") id: Int,
        @Part images: List<MultipartBody.Part>
    )



    @Serializable
    data class CreateMemoryReq(
        val title: String,
        val note: String,
        val mood: Int? = null,
        val tags: List<String> = emptyList(),
        val latitude: Double? = null,
        val longitude: Double? = null,

        @SerializedName("memory_datetime")   // ← Correct for Gson
        val memoryDateTime: String? = null,

        @SerializedName("location_name")     // ← Correct for Gson
        val locationName: String? = null

    )

}