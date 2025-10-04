package com.example.taska.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taska.data.local.entity.Task
import com.example.taska.data.local.model.Date
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с таблицей задач [Task].
 */
@Dao
interface TaskDao {

    /**
     * Получает список задач по дате.
     *
     * @param selectedDate дата для фильтрации
     * @return поток со списком задач
     */
    @Query("SELECT * FROM tasks WHERE date = :selectedDate")
    fun getTasksByDate(selectedDate: Date): Flow<List<Task>>

    /**
     * Добавляет задачу в таблицу.
     *
     * @param task задача
     */
    @Insert
    suspend fun addTask(task: Task)

    /**
     * Удаляет задачу из таблицы.
     *
     * @param task задача
     */
    @Delete
    suspend fun deleteTask(task: Task)

    /**
     * Обновляет задачу в таблице.
     *
     * @param task задача
     */
    @Update
    suspend fun updateTask(task: Task)
}