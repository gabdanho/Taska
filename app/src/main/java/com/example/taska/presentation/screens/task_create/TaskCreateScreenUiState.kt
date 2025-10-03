package com.example.taska.presentation.screens.task_create

import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.model.task.Task

data class TaskCreateScreenUiState(
    val newTask: Task = Task(),
    val imageToShow: String? = null,
    val currentDate: Date? = null,
    val imagesUris: List<String> = emptyList(),
)