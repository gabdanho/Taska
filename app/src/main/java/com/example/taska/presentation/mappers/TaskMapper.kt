package com.example.taska.presentation.mappers

import com.example.taska.presentation.model.task.Task
import com.example.taska.domain.model.Task as TaskDomain

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