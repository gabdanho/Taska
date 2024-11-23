package com.example.taska.ui.screens

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taska.constants.TextFieldType
import com.example.taska.data.Task
import com.example.taska.model.date.Day
import com.example.taska.ui.custom.InputTextField
import com.example.taska.ui.theme.AquaSqueeze

@Composable
fun TaskCreateScreen(
    day: Day,
    onCreateTaskClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var task by remember { mutableStateOf(Task(date = day)) }
    var titleValue by remember { mutableStateOf("") }
    var descriptionValue by remember { mutableStateOf("") }
    val taskFormation = {
        task.copy(
            title = if (titleValue == "") descriptionValue.split(" ").take(3).joinToString(" ") else titleValue,
            description = descriptionValue
        )
    }

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                task = taskFormation()
                onCreateTaskClick(task)
            }
        }
    }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    backDispatcher?.addCallback(backCallback)

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AquaSqueeze)
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
            ) {
                IconButton(
                    onClick = {
                        task = taskFormation()
                        onCreateTaskClick(task)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back screen"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AquaSqueeze)
        ) {
            InputTextField(
                typeField = TextFieldType.TITLE,
                value = titleValue,
                onValueChange = { titleValue = it },
                autoFocus = true,
                modifier = Modifier.padding(12.dp),
                initialValue = task.title
            )
            InputTextField(
                typeField = TextFieldType.DESCRIPTION,
                value = descriptionValue,
                onValueChange = { descriptionValue = it },
                modifier = Modifier.padding(12.dp),
                initialValue = task.title
            )
        }
    }
}

//@Preview
//@Composable
//private fun TaskCreateScreenPreview() {
//    TaskCreateScreen(
//        day = Day(
//            month = Month.NOVEMBER,
//            number = 14,
//            year = 2024,
//            week = DayOfWeek.FRIDAY
//        ),
//        onCreateTaskClick = { }
//    )
//}