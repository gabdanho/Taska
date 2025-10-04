package com.example.taska.data.mappers

import com.example.taska.data.local.entity.Task
import com.example.taska.domain.model.Task as TaskDomain

/**
 * Преобразует [TaskDomain] в [Task].
 *
 * @receiver [TaskDomain]
 * @return [Task]
 */
fun TaskDomain.toDataLayer(): Task {
    return Task(
        id = id,
        date = date.toDataLayer(),
        title= title,
        description = description,
        images = images,
        reminders = reminders.map { it.toDataLayer() }
    )
}

/**
 * Преобразует [Task] в [TaskDomain].
 *
 * @receiver [Task]
 * @return [TaskDomain]
 */
fun Task.toDomainLayer(): TaskDomain {
    return TaskDomain(
        id = id,
        date = date.toDomainLayer(),
        title= title,
        description = description,
        images = images,
        reminders = reminders.map { it.toDomainLayer() }
    )
}