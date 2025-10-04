package com.example.taska.data.mappers

import com.example.taska.data.local.model.Reminder
import com.example.taska.domain.model.Reminder as ReminderDomain

/**
 * Преобразует [ReminderDomain] в [Reminder].
 *
 * @receiver [ReminderDomain]
 * @return [Reminder]
 */
fun ReminderDomain.toDataLayer(): Reminder {
    return Reminder(
        id = id,
        text = text,
        date = date,
        time = time
    )
}

/**
 * Преобразует [Reminder] в [ReminderDomain].
 *
 * @receiver [Reminder]
 * @return [ReminderDomain]
 */
fun Reminder.toDomainLayer(): ReminderDomain {
    return ReminderDomain(
        id = id,
        text = text,
        date = date,
        time = time
    )
}