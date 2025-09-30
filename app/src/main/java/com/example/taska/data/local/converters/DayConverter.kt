package com.example.taska.data.local.converters

import androidx.room.TypeConverter
import com.example.taska.data.local.model.Date
import java.time.DayOfWeek
import java.time.Month

class DayConverter {
    @TypeConverter
    fun toString(day: Date): String = "${day.number}.${day.month.value}.${day.year}.${day.week.value}"

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