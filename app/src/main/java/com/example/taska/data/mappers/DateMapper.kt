package com.example.taska.data.mappers

import com.example.taska.data.local.model.Date
import com.example.taska.domain.model.Date as DateDomain

/**
 * Преобразует [DateDomain] в [Date].
 *
 * @receiver [DateDomain]
 * @return [Date]
 */
fun DateDomain.toDataLayer(): Date {
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