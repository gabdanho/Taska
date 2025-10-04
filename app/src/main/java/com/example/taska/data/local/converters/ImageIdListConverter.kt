package com.example.taska.data.local.converters

import androidx.room.TypeConverter

/**
 * Конвертер для списка идентификаторов изображений.
 */
class ImageIdListConverter {

    /**
     * Преобразует список строк в одну строку.
     *
     * @param list список идентификаторов
     * @return строка с разделителем "||"
     */
    @TypeConverter
    fun toString(list: List<String>): String = list.joinToString(separator = "||")

    /**
     * Преобразует строку в список строк.
     *
     * @param string строковое представление списка
     * @return список строк
     */
    @TypeConverter
    fun toList(string: String): List<String> {
        if (string.isBlank()) return emptyList()
        return string.split("||")
    }
}