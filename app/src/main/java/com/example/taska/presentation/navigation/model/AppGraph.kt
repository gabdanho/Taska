package com.example.taska.presentation.navigation.model

import com.example.taska.presentation.model.task.Date
import kotlinx.serialization.Serializable

/**
 * Состояния навигации приложения.
 */
@Serializable
sealed class AppGraph : NavigationDestination {

    /** Экран всех задач. */
    @Serializable
    data object AllTasksScreen : AppGraph()

    /** Экран создания задачи. */
    @Serializable
    data class TaskCreateScreen(val date: Date = Date()) : AppGraph()
}