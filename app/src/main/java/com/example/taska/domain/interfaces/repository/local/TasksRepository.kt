package com.example.taska.domain.interfaces.repository.local

import com.example.taska.domain.model.Date
import com.example.taska.domain.model.Task

interface TasksRepository {

    suspend fun getTasksByDate(date: Date): List<Task>

    suspend fun addTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)
}