package com.example.taska.presentation.utils

import java.time.DayOfWeek
import java.time.Month

fun Month.convertToText(): String {
    return when(this) {
        Month.JANUARY -> "ЯНВАРЬ"
        Month.FEBRUARY -> "ФЕВРАЛЬ"
        Month.MARCH -> "МАРТ"
        Month.APRIL -> "АПРЕЛЬ"
        Month.MAY -> "МАЙ"
        Month.JUNE -> "ИЮНЬ"
        Month.JULY -> "ИЮЛЬ"
        Month.AUGUST -> "АВГУСТ"
        Month.SEPTEMBER -> "СЕНТЯБРЬ"
        Month.OCTOBER -> "ОКТЯБРЬ"
        Month.NOVEMBER -> "НОЯБРЬ"
        Month.DECEMBER -> "ДЕКАБРЬ"
    }
}

fun DayOfWeek.convertToText(): String {
    return when(this) {
        DayOfWeek.MONDAY -> "ПН"
        DayOfWeek.TUESDAY -> "ВТ"
        DayOfWeek.WEDNESDAY -> "СР"
        DayOfWeek.THURSDAY -> "ЧТ"
        DayOfWeek.FRIDAY -> "ПТ"
        DayOfWeek.SATURDAY -> "СБ"
        DayOfWeek.SUNDAY -> "ВС"
    }
}