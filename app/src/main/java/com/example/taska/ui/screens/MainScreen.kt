package com.example.taska.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taska.constants.TextFieldType
import com.example.taska.data.Task
import com.example.taska.model.date.Day
import com.example.taska.ui.custom.InputTextField
import com.example.taska.ui.theme.AquaSpring
import com.example.taska.ui.theme.BattleshipGrey
import com.example.taska.ui.theme.Brick
import com.example.taska.ui.theme.FruitSalad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MainScreen(
    daysList: List<Day>,
    currentDay: Day,
    tasks: List<Task>,
    isCanRefreshTasks: Boolean,
    changeCurrentDay: (Day) -> Unit,
    updateTask: suspend (Task) -> Unit,
    removeTaskSwipe: suspend (Task) -> Unit,
    onCreateTaskButtonClick: () -> Unit,
    changeRefreshState: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarScreen(
                currentDay = currentDay,
                daysList = daysList,
                changeCurrentDay = changeCurrentDay,
                changeRefreshState = changeRefreshState,
                modifier = Modifier.padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
            )
        }
    ) { innerPadding ->
        TasksField(
            tasks = tasks,
            removeTaskSwipe = removeTaskSwipe,
            isCanRefreshTasks = isCanRefreshTasks,
            changeRefreshState = changeRefreshState,
            updateTask = updateTask,
            modifier = Modifier.padding(innerPadding)
        )
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            FloatingActionButton(
                onClick = {
                    onCreateTaskButtonClick()
                },
                shape = RectangleShape,
                elevation = FloatingActionButtonDefaults.elevation(16.dp),
                containerColor = FruitSalad,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(
                        bottom = WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun TopAppBarScreen(
    currentDay: Day,
    daysList: List<Day>,
    changeRefreshState: (Boolean) -> Unit,
    changeCurrentDay: (Day) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(daysList.indexOf(currentDay))
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.background(color = AquaSpring)
    ) {
        Text(
            text = daysList[scrollState.firstVisibleItemIndex].month.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600,
            fontSize = 22.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        LazyRow(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BattleshipGrey)
                .padding(12.dp)
        ) {
            items(daysList) { day ->
                val borderRadius by animateDpAsState(
                    targetValue = if (currentDay == day) 20.dp else 0.dp,
                    animationSpec = tween(300)
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            focusManager.clearFocus()
                            withContext(Dispatchers.IO) {
                                delay(50)
                                changeCurrentDay(day)
                                changeRefreshState(true)
                            }
                        }
                    },
                    shape = RoundedCornerShape(borderRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AquaSpring
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(
                            if (day != daysList.last()) PaddingValues(end = 12.dp) else PaddingValues(
                                0.dp
                            )
                        )
                        .size(60.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = day.number.toString(),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = day.week.toString().take(2),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksField(
    isCanRefreshTasks: Boolean,
    tasks: List<Task>,
    updateTask: suspend (Task) -> Unit,
    removeTaskSwipe: suspend (Task) -> Unit,
    modifier: Modifier = Modifier,
    changeRefreshState: (Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val displayedTasks = remember { mutableStateListOf<Task>() }
    LaunchedEffect(tasks) {
        if (isCanRefreshTasks) {
            displayedTasks.clear()
            displayedTasks.addAll(tasks)
            changeRefreshState(false)
        }
    }

    LazyColumn(
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        itemsIndexed(displayedTasks, key = { _, task -> task.id }) { i, task ->
            Box {
                var isVisible by rememberSaveable { mutableStateOf(false) }
                var progress by remember { mutableStateOf(0f) }

                LaunchedEffect(Unit) {
                    delay(75L * i)
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = scaleIn(),
                    exit = scaleOut(tween(300))
                ) {
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart && progress >= 0.5f) {
                                coroutineScope.launch {
                                    isVisible = false
                                    delay(300)
                                    removeTaskSwipe(task)
                                    displayedTasks.remove(task)
                                }
                                true
                            } else {
                                false
                            }
                        }
                    )

                    LaunchedEffect(dismissState.progress) {
                        progress = dismissState.progress
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            DismissDeleteBox()
                        }
                    ) {
                        TaskCard(task, updateTask)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    updateTask: suspend (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var displayedTask by remember { mutableStateOf(task) }
    var isExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(displayedTask.title) }
    var description by remember { mutableStateOf(displayedTask.description) }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
            .clickable {
                isExpanded = !isExpanded
            },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = BattleshipGrey)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .animateContentSize(animationSpec = tween(200))
        ) {
            Row {
                if (isExpanded) {
                    IconButton(
                        onClick = { isExpanded = false }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = "Hide card description"
                        )
                    }
                }
                InputTextField(
                    typeField = TextFieldType.TITLE_CARD,
                    initialValue = displayedTask.title,
                    value = title,
                    onValueChange = { title = it },
                    enabled = isExpanded,
                    onLeaveFocus = {
                        coroutineScope.launch {
                            displayedTask = displayedTask.copy(title = title)
                            updateTask(displayedTask)
                        }
                    },
                    modifier = Modifier.padding(12.dp)
                )
            }
            if (isExpanded) {
                InputTextField(
                    typeField = TextFieldType.DESCRIPTION_CARD,
                    initialValue = displayedTask.description,
                    value = description,
                    onValueChange = { description = it },
                    enabled = isExpanded,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    onLeaveFocus = {
                        coroutineScope.launch {
                            displayedTask = displayedTask.copy(description = description)
                            updateTask(displayedTask)
                        }
                    },
                    modifier = Modifier.padding(12.dp)
                )
            } else if (description.isNotEmpty()) {
                Text(
                    text = "...",
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun DismissDeleteBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
            .background(Brick),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Удалить",
            tint = Color.White,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}


//@Preview
//@Composable
//private fun TopAppBarScreenPreview() {
//    TopAppBarScreen(
//        currentDay = Day(
//            month = Month.NOVEMBER,
//            number = 14,
//            year = 2024,
//            week = DayOfWeek.FRIDAY
//        ),
//        daysList = FakeData.daysList,
//        changeCurrentDay = { },
//        changeRefreshState = changeRefreshState
//    )
//}

//@Preview
//@Composable
//private fun TasksFieldPreview() {
//    TasksField(
//        tasks = FakeData.tasksLists,
//        removeTaskSwipe = { }
//    )
//}