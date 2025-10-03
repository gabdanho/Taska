package com.example.taska.data.local.converters

import androidx.room.TypeConverter

class ImageIdListConverter {
    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(separator = "||")
    }

    @TypeConverter
    fun toList(string: String): List<String> {
        if (string.isBlank()) return emptyList()
        return string.split("||")
    }
}