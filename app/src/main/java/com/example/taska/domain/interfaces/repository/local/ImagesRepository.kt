package com.example.taska.domain.interfaces.repository.local

interface ImagesRepository {

    suspend fun saveImage(uri: String, fileName: String)

    suspend fun deleteImage(fileName: String)
}