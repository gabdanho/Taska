package com.example.taska.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taska.presentation.screens.tasks.TasksScreen
import com.example.taska.presentation.screens.task_create.TaskCreateScreen

@Composable
fun TaskaNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.MAIN.name
    ) {
        composable(route = ScreenRoutes.MAIN.name) {
            TasksScreen()
        }

        composable(route = ScreenRoutes.CREATE.name) {
            TaskCreateScreen()
        }
    }

}