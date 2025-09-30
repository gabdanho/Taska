package com.example.taska.domain.model

data class Task(
    val id: Int = 0,
    val date: Date = Date(),
    val title: String = "",
    val description: String = "",
    val imagesId: List<String> = listOf(),
    val reminders: List<Reminder> = listOf(),
)
