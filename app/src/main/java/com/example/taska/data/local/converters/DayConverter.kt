package com.example.taska.data.local.converters

import androidx.room.TypeConverter
import com.example.taska.data.local.model.Date
import java.time.DayOfWeek
import java.time.Month

/**
 * Конвертер для преобразования [Date] в строку и обратно для Room.
 */
class DayConverter {
    /**
     * Преобразует объект [Date] в строку.
     *
     * @param day дата для сериализации
     * @return строковое представление даты
     */
    @TypeConverter
    fun toString(day: Date): String = "${day.number}.${day.month.value}.${day.year}.${day.week.value}"

    /**
     * Преобразует строку в объект [Date].
     *
     * @param value строковое значение даты
     * @return объект [Date]
     */
    @TypeConverter
    fun toDay(value: String): Date {
        val (number, month, year, week) = value.split(".")

        return Date(
            number = number.toInt(),
            month = Month.of(month.toInt()),
            year = year.toInt(),
            week = DayOfWeek.of(week.toInt())
        )
    }
}