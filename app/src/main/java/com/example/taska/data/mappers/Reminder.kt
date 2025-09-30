package com.example.taska.data.mappers

import com.example.taska.data.local.model.Reminder
import com.example.taska.domain.model.Reminder as ReminderDomain

fun ReminderDomain.toDataLayer(): Reminder {
    return Reminder(
        id = id,
        text = text,
        date = date,
        time = time
    )
}

fun Reminder.toDomainLayer(): ReminderDomain {
    return ReminderDomain(
        id = id,
        text = text,
        date = date,
        time = time
    )
}