package com.example.taska.presentation.mappers

import com.example.taska.presentation.model.task.Task
import com.example.taska.domain.model.Task as TaskDomain

/**
 * Преобразует [TaskDomain] в [Task].
 *
 * @receiver [TaskDomain]
 * @return [Task]
 */
fun TaskDomain.toPresentationLayer(): Task {
    return Task(
        id = id,
        date = date.toPresentationLayer(),
        title= title,
        description = description,
        images = images,
        reminders = reminders.map { it.toPresentationLayer() }
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