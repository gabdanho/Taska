package com.example.taska.model.fake

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.taska.data.Task
import com.example.taska.model.date.Day
import java.time.DayOfWeek
import java.time.Month

object FakeData {
    val daysList = mutableListOf<Day>()
    var i = 1

    init {
        while (i <= 30) {
            daysList.add(
                Day(
                    month = Month.NOVEMBER,
                    number = i,
                    year = 2024,
                    week = DayOfWeek.FRIDAY
                )
            )
            i++
        }
        i = 1
        while (i <= 31) {
            daysList.add(
                Day(
                    month = Month.DECEMBER,
                    number = i,
                    year = 2024,
                    week = DayOfWeek.FRIDAY
                )
            )
            i++
        }
    }

    val tasksLists = listOf(
        Task(
            date = Day(
                month = Month.DECEMBER,
                number = 15,
                year = 2024,
                week = DayOfWeek.FRIDAY
            ),
            title = "Приготовить филешку",
            description = "Из курицы"
        ),
        Task(
            date = Day(
                month = Month.DECEMBER,
                number = 15,
                year = 2024,
                week = DayOfWeek.FRIDAY
            ),
            title = "Сделать задачи по физике",
            description = "1204, 1206, 1212"
        )
    )
}