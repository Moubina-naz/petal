package com.example.petal

import android.content.Context
import android.net.Uri
import java.io.File


fun Uri.toFile(context: Context): File {
    val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(this)?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}