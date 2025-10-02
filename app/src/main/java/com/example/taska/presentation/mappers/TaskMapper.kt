package com.example.taska.presentation.mappers

import com.example.taska.presentation.model.task.Task
import com.example.taska.domain.model.Task as TaskDomain

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