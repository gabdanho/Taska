package com.example.taska.domain.model

import java.time.DayOfWeek
import java.time.Month

data class Date(
    val number: Int = 1,
    val month: Month = Month.JANUARY,
    val year: Int = 2000,
    val week: DayOfWeek = DayOfWeek.MONDAY,
)