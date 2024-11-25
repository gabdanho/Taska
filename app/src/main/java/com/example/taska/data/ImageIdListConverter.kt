package com.example.taska.data

import androidx.room.TypeConverter

class ImageIdListConverter {
    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(", ")
    }

    @TypeConverter
    fun toList(string: String): List<String> {
        return string.split(", ")
    }
}