package com.example.taska.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taska.model.date.Day
import com.example.taska.notifications.Reminder
import java.time.DayOfWeek
import java.time.Month

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Day = Day(1, Month.JANUARY, 2024, DayOfWeek.MONDAY),
    val title: String = "",
    val description: String = "",
    val imagesId: List<String> = listOf(),
    val reminders: List<Reminder> = listOf()
)
