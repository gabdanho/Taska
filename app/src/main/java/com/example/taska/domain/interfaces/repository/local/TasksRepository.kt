package com.example.taska.domain.interfaces.repository.local

import com.example.taska.domain.model.Date
import com.example.taska.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    fun getTasksByDate(date: Date): Flow<List<Task>>

    suspend fun addTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)
}