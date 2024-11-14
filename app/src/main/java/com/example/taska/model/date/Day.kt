package com.example.taska.model.date

import java.time.DayOfWeek
import java.time.Month

data class Day(
    val number: Int,
    val month: Month,
    val year: Int,
    val week: DayOfWeek,
)
