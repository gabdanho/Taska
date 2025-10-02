package com.example.taska.presentation.utils

import com.example.taska.presentation.model.task.Date
import java.time.LocalDate

fun isLeap(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
}

fun generateCalendar(): List<Date> {
    val currentYear = LocalDate.now().year
    val totalDays = if (isLeap(currentYear)) 366 else 365
    val daysList = mutableListOf<Date>()

    for (i in 1..totalDays) {
        val localDateDay = LocalDate.ofYearDay(currentYear, i)
        val day = Date(
            number = localDateDay.dayOfMonth,
            month = localDateDay.month,
            year = localDateDay.year,
            week = localDateDay.dayOfWeek
        )
        daysList.add(day)
    }

    return daysList
}