package com.example.taska.di

import android.app.AlarmManager
import android.content.Context
import com.example.taska.data.local.dao.TaskDao
import com.example.taska.data.local.datasource.TaskDatabase
import com.example.taska.data.repository.impl.TasksRepositoryImpl
import com.example.taska.domain.interfaces.repository.local.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleApp {

    @Provides
    @Singleton
    fun provideTaskDatabase(
        @ApplicationContext appContext: Context,
    ): TaskDatabase {
        return TaskDatabase.getDatabase(context = appContext)
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDatabase): TaskDao {
        return db.taskDao()
    }

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext appContext: Context): AlarmManager {
        return appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun provideTasksRepository(taskDao: TaskDao): TasksRepository {
        return TasksRepositoryImpl(taskDao = taskDao)
    }
}