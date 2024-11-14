package com.example.taska

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.taska.ui.navigation.TaskaNavGraph

@Composable
fun TaskaApp(navHostController: NavHostController = rememberNavController()) {
    TaskaNavGraph(navController = navHostController)
}