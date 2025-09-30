package com.example.taska.data.local.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taska.data.local.converters.DayConverter
import com.example.taska.data.local.converters.ImageIdListConverter
import com.example.taska.data.local.converters.RemindersConverter
import com.example.taska.data.local.dao.TaskDao
import com.example.taska.data.local.entity.Task

@TypeConverters(DayConverter::class, ImageIdListConverter::class, RemindersConverter::class)
@Database(entities = [Task::class], version = 6)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDao
    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TaskDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}