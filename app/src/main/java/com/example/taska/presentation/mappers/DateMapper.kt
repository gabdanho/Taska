package com.example.taska.presentation.mappers

import com.example.taska.presentation.model.task.Date
import com.example.taska.domain.model.Date as DateDomain

fun DateDomain.toDataLayer(): Date {
    return Date(
        number = number,
        month = month,
        year = year,
        week = week
    )
}

fun Date.toDomainLayer(): DateDomain {
    return DateDomain(
        number = number,
        month = month,
        year = year,
        week = week
    )
}