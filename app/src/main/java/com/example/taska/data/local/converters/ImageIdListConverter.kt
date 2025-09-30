package com.example.taska.data.local.converters

import androidx.room.TypeConverter

class ImageIdListConverter {
    @TypeConverter
    fun toString(list: List<String>): String = list.joinToString(", ")

    @TypeConverter
    fun toList(string: String): List<String> = string.split(", ")
}