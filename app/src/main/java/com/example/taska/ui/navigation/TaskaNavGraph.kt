package com.example.taska.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taska.ui.screens.MainScreen
import com.example.taska.ui.screens.TaskCreateScreen
import com.example.taska.ui.viewmodel.TaskaViewModel
import kotlinx.coroutines.launch

@Composable
fun TaskaNavGraph(
    viewModel: TaskaViewModel = viewModel(),
    navController: NavHostController
) {
    val uiState = viewModel.uiState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.MAIN.name
    ) {
        composable(route = ScreenRoutes.MAIN.name) {
            MainScreen(
                tasks = uiState.currentTasks,
                daysList = uiState.daysList,
                currentDay = uiState.currentDay,
                onCreateTaskButtonClick = { navController.navigate(ScreenRoutes.CREATE.name) },
                changeCurrentDay = viewModel::changeCurrentDay
            )
        }

        composable(route = ScreenRoutes.CREATE.name) {
            TaskCreateScreen(
                day = uiState.currentDay,
                onCreateTaskClick = { task ->
                    if (task.title == "" && task.description == "") {
                        Toast.makeText(context, "Пустая заметка не была добавлена", Toast.LENGTH_SHORT).show()
                    } else {
                        coroutineScope.launch {
                            viewModel.createTask(task)
                        }
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}