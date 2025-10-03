package com.example.taska.data.utils

import android.content.Context
import android.net.Uri

object ImageSaver {

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