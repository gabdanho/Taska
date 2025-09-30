package com.example.taska.presentation.utils

import android.content.Context
import android.net.Uri

object ImageManager {
    fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    fun deleteImageFromInternalStorage(context: Context, fileName: String) {
        context.deleteFile(fileName)
    }
}