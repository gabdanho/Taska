package com.example.taska.model.date

import java.time.LocalDate

fun isLeap(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
}

fun generateCalendar(currentYear: Int = LocalDate.now().year): List<Day> {
    val totalDays = if (isLeap(currentYear)) 366 else 365
    val daysList = mutableListOf<Day>()

    for (i in 1..totalDays) {
        val localDateDay = LocalDate.ofYearDay(currentYear, i)
        val day = Day(
            number = localDateDay.dayOfMonth,
            month = localDateDay.month,
            year = localDateDay.year,
            week = localDateDay.dayOfWeek
        )
        daysList.add(day)
    }

    return daysList
}