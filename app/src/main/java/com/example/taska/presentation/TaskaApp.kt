package com.example.taska.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.taska.presentation.navigation.TaskaNavGraph

@Composable
fun TaskaApp(navHostController: NavHostController = rememberNavController()) {
    TaskaNavGraph(navController = navHostController)
}