package com.example.taska.domain.interfaces.repository.local

/**
 * Репозиторий для работы с изображениями во внутреннем хранилище.
 */
interface ImagesRepository {

    /**
     * Сохраняет изображение во внутреннее хранилище.
     *
     * @param uri строка с URI изображения
     * @param fileName имя файла, под которым будет сохранено изображение
     */
    suspend fun saveImage(uri: String, fileName: String)

    /**
     * Удаляет изображение из внутреннего хранилища.
     *
     * @param fileName имя файла изображения
     */
    suspend fun deleteImage(fileName: String)
}