package com.example.petal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.petal.data.remote.MemoryApi
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
            MemoryApi.CreateMemoryReq(
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
        val req = MemoryApi.CreateMemoryReq(
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

        println("ACTUAL JSON = " + Gson().toJson(req))
        return MemoryMapper.map(response)
    }


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
        maxWidth: Int = 1200,          // adjust as needed (1080–1600 common)
        maxHeight: Int = 1200,
        quality: Int = 80              // 70–85 is sweet spot
    ): File {
        return withContext(Dispatchers.IO) {
            // Step 1: Decode bitmap with sampling to avoid OOM
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input, null, options)
            }

            // Calculate sample size for downscaling
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false

            val bitmap = context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input, null, options)
            } ?: throw IOException("Failed to decode bitmap")

            // Step 2: Scale if still larger
            val scaledBitmap = if (bitmap.width > maxWidth || bitmap.height > maxHeight) {
                val ratio = min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
                Bitmap.createScaledBitmap(
                    bitmap,
                    (bitmap.width * ratio).toInt(),
                    (bitmap.height * ratio).toInt(),
                    true
                )
            } else {
                bitmap
            }.also { if (it != bitmap) bitmap.recycle() }

            // Step 3: Compress to JPEG bytes
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            scaledBitmap.recycle()

            // Step 4: Write to temp file (Retrofit Multipart needs File)
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

}