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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TaskaNavGraph(
    viewModel: TaskaViewModel = viewModel(),
    navController: NavHostController
) {
    val uiState = viewModel.uiState.collectAsState().value

    if (uiState.isDataLoaded) {
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.MAIN.name
        ) {
            composable(route = ScreenRoutes.MAIN.name) {
                MainScreen(
                    isCanRefreshTasks = uiState.isCanRefreshTasks,
                    tasks = uiState.currentTasks,
                    daysList = uiState.daysList,
                    currentDay = uiState.currentDay,
                    onCreateTaskButtonClick = { navController.navigate(ScreenRoutes.CREATE.name) },
                    changeCurrentDay = viewModel::changeCurrentDay,
                    removeTaskSwipe = viewModel::removeTaskFromDatabase,
                    updateTask = viewModel::updateTask,
                    changeRefreshState = viewModel::changeRefreshState
                )
            }

            composable(route = ScreenRoutes.CREATE.name) {
                TaskCreateScreen(
                    day = uiState.currentDay,
                    getLastTaskId = viewModel::getLastTaskId,
                    toBackScreen = { navController.popBackStack() },
                    changeRefreshState = viewModel::changeRefreshState,
                    createTask = viewModel::createTask
                )
            }
        }
    }
}