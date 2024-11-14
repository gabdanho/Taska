package com.example.taska.ui.viewmodel

import com.example.taska.data.Task
import com.example.taska.model.date.Day
import java.time.LocalDate

data class TaskaUiState(
    val daysList: List<Day> = emptyList(),
    val currentDay: Day = LocalDate.now().let {
        Day(
            month = it.month,
            number = it.dayOfMonth,
            year = it.year,
            week = it.dayOfWeek
        )
    },
    val currentTasks: List<Task> = emptyList()
)
