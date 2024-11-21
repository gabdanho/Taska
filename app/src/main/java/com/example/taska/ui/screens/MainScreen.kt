package com.example.taska.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taska.data.Task
import com.example.taska.model.date.Day
import com.example.taska.ui.theme.AquaSpring
import com.example.taska.ui.theme.BattleshipGrey
import com.example.taska.ui.theme.FruitSalad
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    daysList: List<Day>,
    currentDay: Day,
    tasks: List<Task>,
    isCanRefreshTasks: Boolean,
    changeCurrentDay: (Day) -> Unit,
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
    changeCurrentDay: (Day) -> Unit,
    modifier: Modifier = Modifier,
    changeRefreshState: (Boolean) -> Unit
) {
    val scrollState = rememberLazyListState()

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
                        changeCurrentDay(day)
                        changeRefreshState(true)
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
                    Text(
                        text = day.number.toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksField(
    isCanRefreshTasks: Boolean,
    removeTaskSwipe: suspend (Task) -> Unit,
    tasks: List<Task>,
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
        modifier = modifier.padding(12.dp).fillMaxSize()
    ) {
        itemsIndexed(displayedTasks, key = { _, task -> task.id }) { i, task ->
            Box(Modifier.height(100.dp)) {
                var isVisible by rememberSaveable{ mutableStateOf(false) }

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
                            if (it == SwipeToDismissBoxValue.EndToStart) {
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

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Red),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row {
                                    Text(text = "Удалить", color = Color.White)
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Удалить",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    ) {
                        Card(
                            modifier = Modifier.padding(bottom = 8.dp),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(containerColor = BattleshipGrey)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 12.dp)
                            ) {
                                Text(
                                    text = task.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W400,
                                    color = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Text(
                                    text = task.description,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
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