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

    @GET("memories/by-month/")
    suspend fun getMemoriesByMonth(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Map<String, List<MemoryDto>>

    @Multipart
    @POST("memories/{id}/images/")
    suspend fun uploadMemoryImages(
        @Path("id") id: Int,
        @Part images: List<MultipartBody.Part>
    )

    @Multipart
    @POST("memories/{id}/audio/")
    suspend fun uploadAudio(
        @Path("id") id: Int,
        @Part audio: MultipartBody.Part
    ): MemoryDto
    @DELETE("memories/{id}/audio/")
    suspend fun deleteAudio(@Path("id") id: Int): MemoryDto

    @Serializable
    data class CreateMemoryReq(
        val title: String,
        val note: String,
        val mood: Int? = null,
        val tags: List<String> = emptyList(),
        val latitude: Double? = null,
        val longitude: Double? = null,

        @SerializedName("memory_datetime")
        val memoryDateTime: String? = null,

        @SerializedName("location_name")
        val locationName: String? = null

    )

    data class UserProfileDto(
        val id: Int,
        val username: String,
        val email: String,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("last_name") val lastName: String
    )

    data class UpdateProfileReq(
        val username: String? = null,
        val email: String? = null,
        @SerializedName("first_name") val firstName: String? = null,
        @SerializedName("last_name") val lastName: String? = null
    )

    data class ChangePasswordReq(
        @SerializedName("old_password") val oldPassword: String,
        @SerializedName("new_password") val newPassword: String
    )

    @GET("profile/")
    suspend fun getProfile(): UserProfileDto

    @PATCH("profile/")
    suspend fun updateProfile(@Body body: UpdateProfileReq): UserProfileDto

    @POST("profile/change-password/")
    suspend fun changePassword(@Body body: ChangePasswordReq)
}