package com.example.taska.di

import android.app.AlarmManager
import android.content.Context
import com.example.taska.data.TaskDao
import com.example.taska.data.TaskDatabase
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
        @ApplicationContext app: Context
    ): TaskDatabase {
        return TaskDatabase.getDatabase(app)
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
}