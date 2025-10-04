package com.example.taska.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.navigation.model.AppGraph
import com.example.taska.presentation.navigation.model.nav_type.DateSerializerNavType
import com.example.taska.presentation.screens.task_create.TaskCreateScreen
import com.example.taska.presentation.screens.tasks.TasksScreen
import kotlin.reflect.typeOf

/**
 * Построение навграфа приложения.
 *
 * @param modifier модификатор для экранов
 */
fun NavGraphBuilder.appGraph(
    modifier: Modifier = Modifier
) {
    composable<AppGraph.AllTasksScreen> {
        TasksScreen(modifier = modifier)
    }

    composable<AppGraph.TaskCreateScreen>(
        typeMap = mapOf(typeOf<Date>() to DateSerializerNavType())
    ) {
        val args = it.toRoute<AppGraph.TaskCreateScreen>()
        TaskCreateScreen(
            date = args.date,
            modifier = modifier
        )
    }
}