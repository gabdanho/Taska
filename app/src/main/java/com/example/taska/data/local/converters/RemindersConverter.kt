package com.example.taska.data.local.converters

import androidx.room.TypeConverter
import com.example.taska.data.local.model.Reminder

/**
 * Конвертер для списка напоминаний [Reminder].
 */
class RemindersConverter {

    /**
     * Преобразует список напоминаний в строку.
     *
     * @param list список напоминаний
     * @return строка с данными
     */
    @TypeConverter
    fun toString(list: List<Reminder>): String {
        return list.joinToString("; ") {
            "${it.id}|${it.text}|${it.date}|${it.time}"
        }
    }

    /**
     * Преобразует строку в список напоминаний.
     *
     * @param value строковое представление напоминаний
     * @return список [Reminder]
     */
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