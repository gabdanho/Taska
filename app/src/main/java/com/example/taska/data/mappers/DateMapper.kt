package com.example.taska.data.mappers

import com.example.taska.data.local.model.Date
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