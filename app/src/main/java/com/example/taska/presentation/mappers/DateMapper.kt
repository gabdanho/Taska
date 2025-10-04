package com.example.taska.presentation.mappers

import com.example.taska.presentation.model.task.Date
import com.example.taska.domain.model.Date as DateDomain

/**
 * Преобразует [DateDomain] в [Date].
 *
 * @receiver [DateDomain]
 * @return [Date]
 */
fun DateDomain.toPresentationLayer(): Date {
    return Date(
        number = number,
        month = month,
        year = year,
        week = week
    )
}

/**
 * Преобразует [Date] в [DateDomain].
 *
 * @receiver [Date]
 * @return [DateDomain]
 */
fun Date.toDomainLayer(): DateDomain {
    return DateDomain(
        number = number,
        month = month,
        year = year,
        week = week
    )
}