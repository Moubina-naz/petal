package com.example.petal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.petal.data.remote.MemoryApi
import com.example.petal.data.remote.MemoryApi.*
import com.example.petal.domain.Location
import com.example.petal.domain.Memory
import com.example.petal.domain.Mood
import com.example.petal.ui.editMemory.EditMemoryReq
import com.example.petal.ui.homeScreen.HomeFilter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.mapIndexed
import kotlin.math.min
import androidx.core.graphics.scale

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

        println("REPO: Sending location_name = '${memory.location?.name ?: "NULL"}'")
        println("REPO: latitude = ${memory.location?.latitude}, longitude = ${memory.location?.longitude}")

        val response = memoryApi.createMemoryJson(
            CreateMemoryReq(
                title = memory.title,
                note = memory.note,
                mood = memory.mood?.value,
                tags = memory.tags,
                latitude = memory.location?.latitude,
                longitude = memory.location?.longitude,
                locationName = memory.location?.name,
                memoryDateTime = memory.memoryDateTime?.toString()
            )
        )
        println("REPO: Server responded with location_name = '${response.locationName ?: "NULL"}'")
        val req = CreateMemoryReq(
            title = memory.title,
            note = memory.note,
            mood = memory.mood?.value,
            tags = memory.tags,
            latitude = memory.location?.latitude,
            longitude = memory.location?.longitude,
            locationName = memory.location?.name,
            memoryDateTime = memory.memoryDateTime?.let {
                java.time.OffsetDateTime
                    .ofInstant(it, ZoneId.systemDefault())
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            }
        )
        return MemoryMapper.map(response)

        return MemoryMapper.map(response)
    }
    suspend fun uploadAudio(
        context: Context,
        memoryId: Int,
        audioUri: Uri
    ): Memory {
        val audioFile = getFileFromUri(context, audioUri)
        val (fileExtension, mimeType) = getMimeTypeForAudio(audioUri, context)

        val part = MultipartBody.Part.createFormData(
            name = "audio",
            filename = "audio_${System.currentTimeMillis()}.$fileExtension",
            body = audioFile.asRequestBody(mimeType.toMediaTypeOrNull())
        )

        try {
            val response = memoryApi.uploadAudio(memoryId, part)
            return MemoryMapper.map(response)
        } finally {
            audioFile.delete()
        }
    }
    suspend fun deleteAudio(memoryId: Int): Memory {
        val response = memoryApi.deleteAudio(memoryId)
        return MemoryMapper.map(response)
    }

    suspend fun getProfile() = memoryApi.getProfile()
    suspend fun updateProfile(username: String?, email: String?, firstName: String?, lastName: String?) =
        memoryApi.updateProfile(UpdateProfileReq(username, email, firstName, lastName))
    suspend fun changePassword(oldPassword: String, newPassword: String) =
        memoryApi.changePassword(ChangePasswordReq(oldPassword, newPassword))

    suspend fun uploadMemoryImages(
        context: Context,
        memoryId: Int,
        imageUris: List<Uri>
    ) {
        if (imageUris.isEmpty()) return

        val compressedFiles = mutableListOf<File>()

        val parts = imageUris.mapIndexed { index, uri ->
            val compressedFile = compressImageUri(context, uri, maxWidth = 1200, quality = 80)
            compressedFiles.add(compressedFile)  // track for cleanup
            MultipartBody.Part.createFormData(
                name = "image_files",
                filename = "image_${index}_${System.currentTimeMillis()}.jpg",
                body = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
        }

        try {
            memoryApi.uploadMemoryImages(id = memoryId, images = parts)
        } finally {
            compressedFiles.forEach { it.delete() }  // always cleans up, even on failure
        }
    }

    suspend fun getMemoriesByMonth(year: Int, month: Int): Map<Int, List<Memory>> {
        val response = memoryApi.getMemoriesByMonth(year, month)
        return response.mapKeys { it.key.toInt() }
            .mapValues { entry -> entry.value.map(MemoryMapper::map) }
    }

    suspend fun deleteMemory(id: Int) {
        memoryApi.deleteMemory(id)
    }
    suspend fun updateMemory(
        id: Int,
        title: String,
        note: String,
        mood: Mood?,
        tags: List<String>,
        location: Location?,
        memoryDateTime: Instant?

    ) {
        memoryApi.updateMemory(
            id = id,
            body = EditMemoryReq(
                title = title,
                note = note,
                mood = mood?.value,
                tags = tags,
                latitude = location?.latitude,
                longitude = location?.longitude,
                locationName = location?.name,
                memoryDateTime = memoryDateTime?.toString()
            )
        )
    }

    suspend fun compressImageUri(
        context: Context,
        uri: Uri,
        maxWidth: Int = 1200,
        maxHeight: Int = 1200,
        quality: Int = 80
    ): File {
        return withContext(Dispatchers.IO) {
            // decode bitmap
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input, null, options)
            }

            // calculate sample size for downscaling
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false

            val bitmap = context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input, null, options)
            } ?: throw IOException("Failed to decode bitmap")

            // Step 2: Scale if still larger
            val scaledBitmap = if (bitmap.width > maxWidth || bitmap.height > maxHeight) {
                val ratio = min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
                bitmap.scale((bitmap.width * ratio).toInt(), (bitmap.height * ratio).toInt())
            } else {
                bitmap
            }.also { if (it != bitmap) bitmap.recycle() }

            // Step 3: Compress to JPEG bytes
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            scaledBitmap.recycle()

            val tempFile = File.createTempFile("compressed_", ".jpg", context.cacheDir)
            FileOutputStream(tempFile).use { fos ->
                fos.write(outputStream.toByteArray())
            }

            tempFile
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
    private fun getMimeTypeForAudio(uri: Uri, context: Context): Pair<String, String> {
        val contentType = context.contentResolver.getType(uri)?.lowercase() ?: "audio/mpeg"
        return when {
            contentType.contains("mpeg") || contentType.contains("mp3") -> "mp3" to "audio/mpeg"
            contentType.contains("wav") -> "wav" to "audio/wav"
            contentType.contains("ogg") -> "ogg" to "audio/ogg"
            contentType.contains("mp4") || contentType.contains("m4a") -> "m4a" to "audio/mp4"
            else -> "mp3" to "audio/mpeg"  // fallback
        }
    }
    private suspend fun getFileFromUri(context: Context, uri: Uri): File {
        return withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IOException("Cannot open audio file")

            val tempFile = File.createTempFile("audio_", ".tmp", context.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        }
    }

}