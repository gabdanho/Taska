package com.example.taska.presentation.screens.task_create

import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.model.task.Task

/**
 * UI-состояние для [TaskCreateScreen].
 *
 * @param newTask текущая задача
 * @param imageToShow выбранное изображение для просмотра
 * @param currentDate текущая дата
 * @param imagesUris список uri изображений
 */
data class TaskCreateScreenUiState(
    val newTask: Task = Task(),
    val imageToShow: String? = null,
    val currentDate: Date? = null,
    val imagesUris: List<String> = emptyList(),
)