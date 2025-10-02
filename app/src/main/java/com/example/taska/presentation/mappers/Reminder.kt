package com.example.taska.presentation.mappers

import com.example.taska.presentation.model.task.Reminder
import com.example.taska.domain.model.Reminder as ReminderDomain

fun ReminderDomain.toPresentationLayer(): Reminder {
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