package com.example.taska.presentation.model.task

import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.Month

@Serializable
data class Date(
    val number: Int = 1,
    val month: Month = Month.JANUARY,
    val year: Int = 2000,
    val week: DayOfWeek = DayOfWeek.MONDAY,
)