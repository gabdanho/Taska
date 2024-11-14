package com.example.taska.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.taska.model.date.Day
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE date = :selectedDate")
    fun getTasksByDate(selectedDate: Day): Flow<List<Task>>

    @Insert
    suspend fun addTask(task: Task)
}