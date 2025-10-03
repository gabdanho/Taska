package com.example.taska.presentation.screens.tasks

import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.model.task.Reminder
import com.example.taska.presentation.model.task.Task
import java.time.LocalDate
import java.time.LocalTime

data class TasksScreenUiState(
    val currentDay: Date = LocalDate.now().let {
        Date(
            month = it.month,
            number = it.dayOfMonth,
            year = it.year,
            week = it.dayOfWeek
        )
    },
    val isShowDeleteReminderDialog: Boolean = false,
    val isShowDatePicker: Boolean = false,
    val isShowTimePicker: Boolean = false,
    val isShowDateTimePicker: Boolean = false,
    val selectedReminder: Reminder? = null,
    val selectedTask: Task? = null,
    val daysList: List<Date> = emptyList(),
    val imageToShow: String? = null,

    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
)