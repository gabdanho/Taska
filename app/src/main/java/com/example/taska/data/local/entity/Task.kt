package com.example.taska.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taska.data.local.model.Date
import com.example.taska.data.local.model.Reminder
import java.time.DayOfWeek
import java.time.Month

/**
 * Сущность задачи.
 *
 * @property id идентификатор задачи
 * @property date дата задачи
 * @property title заголовок
 * @property description описание
 * @property images список изображений
 * @property reminders список напоминаний
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Date = Date(1, Month.JANUARY, 2024, DayOfWeek.MONDAY),
    val title: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val reminders: List<Reminder> = emptyList(),
)