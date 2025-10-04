package com.example.taska.presentation.model.task

/**
 * Задача.
 *
 * @property id идентификатор задачи
 * @property date дата задачи
 * @property title заголовок
 * @property description описание
 * @property images список изображений
 * @property reminders список напоминаний
 */
data class Task(
    val id: Int = 0,
    val date: Date = Date(),
    val title: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val reminders: List<Reminder> = emptyList(),
)
