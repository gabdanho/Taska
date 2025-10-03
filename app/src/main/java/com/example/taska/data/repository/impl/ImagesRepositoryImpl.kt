package com.example.taska.data.repository.impl

import android.content.Context
import androidx.core.net.toUri
import com.example.taska.data.utils.ImageSaver
import com.example.taska.domain.interfaces.repository.local.ImagesRepository

class ImagesRepositoryImpl(
    private val context: Context,
) : ImagesRepository {

    override suspend fun saveImage(uri: String, fileName: String) {
        ImageSaver.saveImageToInternalStorage(
            context = context,
            uri = uri.toUri(),
            fileName = fileName
        )
    }

    override suspend fun deleteImage(fileName: String) {
        ImageSaver.deleteImageFromInternalStorage(
            context = context,
            fileName = fileName
        )
    }
}