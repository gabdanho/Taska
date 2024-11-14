package com.example.taska.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.taska.model.date.Day
import java.time.DayOfWeek
import java.time.Month

class DayConverter {
    @TypeConverter
    fun toString(day: Day): String {
        return "${day.number}.${day.month.value}.${day.year}.${day.week.value}"
    }

    @TypeConverter
    fun toDay(value: String): Day {
        val (number, month, year, week) = value.split(".")

        return Day(number.toInt(), Month.of(month.toInt()), year.toInt(), DayOfWeek.of(week.toInt()))
    }
}