package com.example.taska.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.taska.presentation.navigation.NavigationAction
import com.example.taska.presentation.navigation.ObserveAsEvents
import com.example.taska.presentation.navigation.appGraph

/**
 * Основной экран приложения.
 *
 * @param modifier модификатор
 * @param viewModel ViewModel
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel<MainScreenViewModel>(),
) {
    val navigator = viewModel.navigator
    val startDestination = navigator.startDestination
    val navController = rememberNavController()

    ObserveAsEvents(flow = navigator.navigationActions) { action ->
        when (action) {
            is NavigationAction.Navigate -> navController.navigate(
                action.navigationDestination
            ) {
                action.navOptions
            }
            NavigationAction.NavigateToPopBackStack -> navController.popBackStack()
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        contentAlignment = Alignment.TopCenter
    ) {
        appGraph(modifier = modifier)
    }
}