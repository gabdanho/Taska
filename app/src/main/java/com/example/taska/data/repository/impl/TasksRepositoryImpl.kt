package com.example.taska.data.repository.impl

import com.example.taska.data.local.dao.TaskDao
import com.example.taska.data.mappers.toDataLayer
import com.example.taska.data.mappers.toDomainLayer
import com.example.taska.domain.interfaces.repository.local.TasksRepository
import com.example.taska.domain.model.Date
import com.example.taska.domain.model.Task

class TasksRepositoryImpl(
    private val taskDao: TaskDao,
) : TasksRepository {

    override suspend fun getTasksByDate(date: Date): List<Task> {
        val mappedData = date.toDataLayer()
        return taskDao.getTasksByDate(selectedDate = mappedData).map { it.toDomainLayer() }
    }

    override suspend fun addTask(task: Task) {
        taskDao.addTask(task = task.toDataLayer())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task = task.toDataLayer())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task = task.toDataLayer())
    }
}