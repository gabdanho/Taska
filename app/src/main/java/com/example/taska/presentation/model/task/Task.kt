package com.example.taska.presentation.model.task

data class Task(
    val id: Int = 0,
    val date: Date = Date(),
    val title: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val reminders: List<Reminder> = emptyList(),
)
