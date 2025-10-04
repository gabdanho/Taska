package com.example.taska.domain.model

import java.time.DayOfWeek
import java.time.Month

/**
 * Модель даты.
 *
 * @property number число месяца
 * @property month месяц
 * @property year год
 * @property week день недели
 */
data class Date(
    val number: Int = 1,
    val month: Month = Month.JANUARY,
    val year: Int = 2000,
    val week: DayOfWeek = DayOfWeek.MONDAY,
)