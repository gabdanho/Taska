package com.example.taska.presentation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.taska.R
import com.example.taska.presentation.constants.TextFieldType
import com.example.taska.data.local.entity.Task
import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.notifications.Reminder
import com.example.taska.presentation.custom.InputTextField
import com.example.taska.presentation.theme.AquaSpring
import com.example.taska.presentation.theme.BattleshipGrey
import com.example.taska.presentation.theme.Brick
import com.example.taska.presentation.theme.FruitSalad
import com.example.taska.presentation.utils.ImageManager.deleteImageFromInternalStorage
import com.example.taska.presentation.utils.ImageManager.saveImageToInternalStorage
import com.example.taska.presentation.utils.translateToRus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Calendar

@Composable
fun MainScreen(
    onBackPressed: () -> Unit = { },
    daysList: List<Date>,
    currentDay: Date,
    tasks: List<Task>,
    isCanRefreshTasks: Boolean,
    changeCurrentDay: (Date) -> Unit,
    updateTask: suspend (Task) -> Unit,
    removeTaskSwipe: suspend (Task) -> Unit,
    onCreateTaskButtonClick: () -> Unit,
    changeTime: (Int, Int) -> Unit,
    changeDate: (Int, Int, Int) -> Unit,
    changeRefreshState: (Boolean) -> Unit,
    addReminder: (Context, Task) -> Reminder?,
    removeReminder: (Task, Reminder, Context) -> Unit,
    changeReminderText: (String) -> Unit
) {
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    backDispatcher?.addCallback(backCallback)

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
            changeDate = changeDate,
            changeTime = changeTime,
            addReminder = addReminder,
            changeReminderText = changeReminderText,
            removeReminder = removeReminder,
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
    currentDay: Date,
    daysList: List<Date>,
    changeRefreshState: (Boolean) -> Unit,
    changeCurrentDay: (Date) -> Unit,
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
            text = daysList[scrollState.firstVisibleItemIndex].month.translateToRus(),
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
                            if (day != daysList.last()) PaddingValues(end = 12.dp) else PaddingValues(0.dp)
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
                            text = day.week.translateToRus(),
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
    changeTime: (Int, Int) -> Unit,
    changeDate: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    changeRefreshState: (Boolean) -> Unit,
    addReminder: (Context, Task) -> Reminder?,
    removeReminder: (Task, Reminder, Context) -> Unit,
    changeReminderText: (String) -> Unit
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
                        },
                        enableDismissFromStartToEnd = false
                    ) {
                        TaskCard(task, updateTask, changeDate, changeTime, addReminder, removeReminder, changeReminderText)
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
    changeDate: (Int, Int, Int) -> Unit,
    changeTime: (Int, Int) -> Unit,
    addReminder: (Context, Task) -> Reminder?,
    removeReminder: (Task, Reminder, Context) -> Unit,
    changeReminderText: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var showDateTimePicker by remember { mutableStateOf(false) }
    var showDeleteReminderDialog by remember { mutableStateOf(false) }

    var displayedTask by remember { mutableStateOf(task) }
    var isExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(displayedTask.title) }
    var description by remember { mutableStateOf(displayedTask.description) }
    val imagesLinks = remember { mutableStateListOf(*task.imagesId.toTypedArray()) }.apply {
        if (this.size == 1 && this[0] == "") removeAt(0)
    }
    val reminders = remember { mutableStateListOf(*task.reminders.toTypedArray()) }
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }

    var showImage by remember { mutableStateOf(false) }
    var selectedImageLink by remember { mutableStateOf("") }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val imageId = if (imagesLinks.isNotEmpty()) imagesLinks.last().replace(".jpg", "").takeLastWhile { it != '-' }.toInt() + 1 else 1
                val fileName = "${displayedTask.id}-${imageId}.jpg"
                imagesLinks.add(fileName)
                saveImageToInternalStorage(context, uri, fileName)
                displayedTask = displayedTask.copy(imagesId = imagesLinks)
                updateTask(displayedTask)
            }
        } else {
            Toast.makeText(context, "Изображение не было выбрано", Toast.LENGTH_SHORT).show()
        }
    }

    // Dialog about delete reminder
    if (showDeleteReminderDialog) {
        Dialog(onDismissRequest = { showDeleteReminderDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AquaSpring
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Удалить уведомление на ${selectedReminder?.date} ${selectedReminder?.time}?",
                        modifier = Modifier.padding(8.dp)
                    )
                    Button(
                        onClick = {
                            removeReminder(displayedTask, selectedReminder!!, context)
                            reminders.remove(selectedReminder)
                            showDeleteReminderDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BattleshipGrey
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
    }

    // Date And Time picker
    if (showDateTimePicker) {
        DateAndTimePicker(
            task,
            changeDate,
            changeTime,
            { showDateTimePicker = false },
            addReminder,
            changeReminderText
        ) { reminder ->
            reminders.add(reminder)
        }
    }

    // Image full screen
    if (showImage) {
        Dialog(onDismissRequest = { showImage = false }) {
            Image(
                painter = rememberAsyncImagePainter(File(context.filesDir, selectedImageLink)),
                contentDescription = "image",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

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
                if (imagesLinks.isNotEmpty()) {
                    LazyRow(modifier = Modifier.padding(top = 8.dp)) {
                        items(imagesLinks) { imageLink ->
                            Box(modifier = Modifier.size(300.dp)) {
                                Image(
                                    painter = rememberAsyncImagePainter(File(context.filesDir, imageLink)),
                                    contentDescription = "image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(300.dp)
                                        .padding(end = 4.dp)
                                        .clickable {
                                            showImage = true
                                            selectedImageLink = imageLink
                                        }
                                )
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Delete image",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.TopEnd)
                                        .clickable {
                                            coroutineScope.launch {
                                                imagesLinks.remove(imageLink)
                                                deleteImageFromInternalStorage(context, imageLink)
                                                displayedTask =
                                                    displayedTask.copy(imagesId = imagesLinks)
                                                updateTask(displayedTask)
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
                if (reminders.isNotEmpty()) {
                    LazyRow(modifier = Modifier.padding(top = 8.dp)) {
                        items(reminders) { reminder ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = AquaSpring
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                selectedReminder = reminder
                                                showDeleteReminderDialog = true
                                            }
                                        )
                                    }
                            ) {
                                Text(
                                    text = "${reminder.date} ${reminder.time}",
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }
                    }
                }
                Row {
                    IconButton(onClick = { galleryLauncher.launch("image/*") }, modifier = Modifier.size(50.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.add_img),
                            tint = Color.White,
                            contentDescription = "Add image",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    IconButton(onClick = { showDateTimePicker = true }, modifier = Modifier.size(50.dp)) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            tint = Color.White,
                            contentDescription = "Add notification",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }
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
fun DateAndTimePicker(
    task: Task,
    changeDate: (Int, Int, Int) -> Unit,
    changeTime: (Int, Int) -> Unit,
    changeShowPicker: (Boolean) -> Unit,
    addReminder: (Context, Task) -> Reminder?,
    changeReminderText: (String) -> Unit,
    updateLocalTaskReminders: (Reminder) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(true) }
    var showTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(
        context,
        {
            _: DatePicker,
            selectedYear: Int,
            selectedMonth: Int,
            selectedDay: Int ->
                showDatePicker = false
                showTimePicker = true
                changeDate(selectedYear, selectedMonth, selectedDay)
        },
        year, month, day
    ).apply {
        setOnCancelListener {
            showDatePicker = false
            changeShowPicker(false)
        }
        setOnDismissListener {
            changeShowPicker(false)
        }
    }

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val timePickerDialog = TimePickerDialog(
        context,
        {
            _: TimePicker,
            selectedHour: Int,
            selectedMinute: Int ->
                changeTime(selectedHour, selectedMinute)
                changeReminderText(task.title)
                val reminder = addReminder(context, task)
                updateLocalTaskReminders(reminder!!)
                showTimePicker = false
                changeShowPicker(false)
        },
        hour, minute, true
    ).apply {
        setOnCancelListener {
            showDatePicker = false
            showTimePicker = false
            changeShowPicker(false)
        }
        setOnDismissListener {
            showDatePicker = false
            showTimePicker = false
            changeShowPicker(false)
        }
    }

    if (showDatePicker) {
        datePickerDialog.show()
    }

    if (showTimePicker) {
        timePickerDialog.show()
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