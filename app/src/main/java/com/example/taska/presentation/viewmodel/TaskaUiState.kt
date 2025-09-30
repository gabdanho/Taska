package com.example.taska.presentation.viewmodel

import com.example.taska.data.local.entity.Task
import com.example.taska.presentation.model.task.Date
import java.time.LocalDate

data class TaskaUiState(
    val daysList: List<Date> = emptyList(),
    val currentDay: Date = LocalDate.now().let {
        Date(
            month = it.month,
            number = it.dayOfMonth,
            year = it.year,
            week = it.dayOfWeek
        )
    },
    val currentTasks: List<Task> = emptyList(),
    val isCanRefreshTasks: Boolean = true,
    val isDataLoaded: Boolean = false,
    val time: String = "",
    val date: String = "",
    val text: String = ""
)
