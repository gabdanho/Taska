package com.example.taska.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taska.data.Task
import com.example.taska.model.date.Day
import com.example.taska.model.fake.FakeData
import com.example.taska.ui.theme.AquaSpring
import com.example.taska.ui.theme.AquaSqueeze
import com.example.taska.ui.theme.BattleshipGrey
import com.example.taska.ui.theme.FruitSalad
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.Month

@Composable
fun MainScreen(
    daysList: List<Day>,
    currentDay: Day,
    tasks: List<Task>,
    changeCurrentDay: (Day) -> Unit,
    onCreateTaskButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarScreen(
                currentDay = currentDay,
                daysList = daysList,
                changeCurrentDay = changeCurrentDay,
                modifier = Modifier.padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            )
        }
    ) { innerPadding ->
        TasksField(
            tasks = tasks,
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
                    .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
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
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        scrollState.animateScrollToItem(daysList.indexOf(currentDay))
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
                    },
                    shape = RoundedCornerShape(borderRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AquaSpring
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(if (day != daysList.last()) PaddingValues(end = 12.dp) else PaddingValues(0.dp))
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

@Composable
fun TasksField(
    tasks: List<Task>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AquaSpring)
            .padding(12.dp)
            .background(color = AquaSqueeze)
    ) {
        LazyColumn(
            modifier = Modifier.padding(12.dp)
        ) {
            itemsIndexed(tasks) { i, task ->
                var isVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(75L * i)
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = scaleIn()
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

@Preview
@Composable
private fun TopAppBarScreenPreview() {
    TopAppBarScreen(
        currentDay = Day(
            month = Month.NOVEMBER,
            number = 14,
            year = 2024,
            week = DayOfWeek.FRIDAY
        ),
        daysList = FakeData.daysList,
        changeCurrentDay = { }
    )
}

@Preview
@Composable
private fun TasksFieldPreview() {
    TasksField(
        tasks = FakeData.tasksLists
    )
}