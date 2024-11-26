package com.example.taska.data

import androidx.room.TypeConverter
import com.example.taska.notifications.Reminder

class RemindersConverter {
    @TypeConverter
    fun toString(list: List<Reminder>): String {
        return list.joinToString("; ") {
            "${it.id}|${it.text}|${it.date}|${it.time}"
        }
    }

    @TypeConverter
    fun toList(value: String): List<Reminder> {
        if (value.isBlank()) return emptyList()

        return value.split("; ").map { part ->
            val fields = part.split("|")
            if (fields.size != 4) {
                throw IllegalArgumentException("Unknown format: $part")
            }
            Reminder(
                id = fields[0].toInt(),
                text = fields[1],
                date = fields[2],
                time = fields[3]
            )
        }
    }
}