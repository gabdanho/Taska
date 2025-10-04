package com.example.taska.presentation.screens.tasks

import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.model.task.Reminder
import com.example.taska.presentation.model.task.Task
import java.time.LocalDate
import java.time.LocalTime

/**
 * UI-состояние для [TasksScreen].
 *
 * @param currentDay выбранный день
 * @param isShowDeleteReminderDialog флаг отображения диалога удаления напоминания
 * @param isShowDatePicker флаг отображения выбора даты
 * @param isShowTimePicker флаг отображения выбора времени
 * @param selectedReminder выбранное напоминание
 * @param selectedTask выбранная задача
 * @param daysList список дней для календаря
 * @param imageToShow изображение для просмотра
 * @param selectedDate выбранная дата для нового напоминания
 * @param selectedTime выбранное время для нового напоминания
 */
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
    val selectedReminder: Reminder? = null,
    val selectedTask: Task? = null,
    val daysList: List<Date> = emptyList(),
    val imageToShow: String? = null,

    val selectedDate: LocalDate? = null,
    val selectedTime: LocalTime? = null,
)