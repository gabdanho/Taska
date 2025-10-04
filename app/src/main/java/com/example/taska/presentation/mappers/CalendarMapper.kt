package com.example.taska.presentation.mappers

import com.example.taska.presentation.model.StringResNamePresentation
import java.time.DayOfWeek
import java.time.Month

/**
 * Конвертирует [Month] в [StringResNamePresentation].
 *
 * @return ресурс строки для названия месяца
 */
fun Month.convertToStringResName(): StringResNamePresentation {
    return when(this) {
        Month.JANUARY -> StringResNamePresentation.TEXT_JANUARY
        Month.FEBRUARY -> StringResNamePresentation.TEXT_FEBRUARY
        Month.MARCH -> StringResNamePresentation.TEXT_MARCH
        Month.APRIL -> StringResNamePresentation.TEXT_APRIL
        Month.MAY -> StringResNamePresentation.TEXT_MAY
        Month.JUNE -> StringResNamePresentation.TEXT_JUNE
        Month.JULY -> StringResNamePresentation.TEXT_JULY
        Month.AUGUST -> StringResNamePresentation.TEXT_AUGUST
        Month.SEPTEMBER -> StringResNamePresentation.TEXT_SEPTEMBER
        Month.OCTOBER -> StringResNamePresentation.TEXT_OCTOBER
        Month.NOVEMBER -> StringResNamePresentation.TEXT_NOVEMBER
        Month.DECEMBER -> StringResNamePresentation.TEXT_DECEMBER
    }
}

/**
 * Конвертирует [DayOfWeek] в [StringResNamePresentation].
 *
 * @return ресурс строки для названия дня недели
 */
fun DayOfWeek.convertToStringResName(): StringResNamePresentation {
    return when(this) {
        DayOfWeek.MONDAY -> StringResNamePresentation.TEXT_MONDAY
        DayOfWeek.TUESDAY -> StringResNamePresentation.TEXT_TUESDAY
        DayOfWeek.WEDNESDAY -> StringResNamePresentation.TEXT_WEDNESDAY
        DayOfWeek.THURSDAY -> StringResNamePresentation.TEXT_THURSDAY
        DayOfWeek.FRIDAY -> StringResNamePresentation.TEXT_FRIDAY
        DayOfWeek.SATURDAY -> StringResNamePresentation.TEXT_SATURDAY
        DayOfWeek.SUNDAY -> StringResNamePresentation.TEXT_SUNDAY
    }
}