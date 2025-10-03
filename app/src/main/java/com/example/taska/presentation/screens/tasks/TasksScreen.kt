package com.example.taska.presentation.screens.tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.taska.R
import com.example.taska.presentation.components.FileImageDialog
import com.example.taska.presentation.components.FileImageItem
import com.example.taska.presentation.constants.TextFieldType
import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.components.InputTextField
import com.example.taska.presentation.model.task.Reminder
import com.example.taska.presentation.model.task.Task
import com.example.taska.presentation.theme.AquaSpring
import com.example.taska.presentation.theme.BattleshipGrey
import com.example.taska.presentation.theme.Brick
import com.example.taska.presentation.theme.FruitSalad
import com.example.taska.presentation.utils.convertToText
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksScreenViewModel = hiltViewModel<TasksScreenViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val displayedTasks by viewModel.displayedTasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarScreen(
                currentDay = uiState.currentDay,
                daysList = uiState.daysList,
                changeCurrentDay = { viewModel.changeCurrentDay(it) },
            )
        },
        modifier = modifier
    ) { innerPadding ->
        TasksField(
            displayedTasks = displayedTasks,
            deleteTask = { viewModel.deleteTask(it) },
            onTitleChange = { task, title -> viewModel.onTitleChange(task, title) },
            onDescriptionChange = { task, description ->
                viewModel.onDescriptionChange(task, description)
            },
            onImageClick = { viewModel.changeImageToShow(it) },
            deleteImage = { task, image -> viewModel.deleteImage(task, image) },
            showDeleteReminderDialog = { task, reminder ->
                viewModel.changeIsShowDeleteReminderDialog(
                    isShow = true,
                    reminder = reminder,
                    task = task
                )
            },
            addImage = { task, image -> viewModel.addImage(task, image) },
            openDatePicker = { viewModel.changeIsShowDateTimePicker(true) },
            modifier = Modifier.padding(innerPadding)
        )
        AddFloatingActionButton(
            onCreateTaskButtonClick = { viewModel.onCreateTaskClick() },
            modifier = Modifier.padding(8.dp)
        )
    }

    // Dialog about delete reminder
    if (uiState.isShowDeleteReminderDialog) {
        uiState.selectedReminder?.let {
            DeleteReminderDialog(
                selectedReminder = it,
                deleteReminder = { viewModel.deleteReminder() },
                onDismiss = {
                    viewModel.changeIsShowDeleteReminderDialog(
                        isShow = false,
                        reminder = null,
                        task = null
                    )
                }
            )
        }
    }

    // Date And Time picker
    if (uiState.isShowDateTimePicker) {
        DateAndTimePicker(
            isShowDatePicker = uiState.isShowDatePicker,
            isShowTimePicker = uiState.isShowTimePicker,
            onDismissDate = { viewModel.changeIsShowDatePicker(false) },
            onDismissTime = { viewModel.changeIsShowTimePicker(false) },
            onDatePicked = { date -> viewModel.onDatePicked(date = date) },
            onTimePicked = { time -> viewModel.onTimePicked(time = time) },
        )
    }

    // Image full screen
    uiState.imageToShow?.let { image ->
        FileImageDialog(
            image = image,
            onDismiss = { viewModel.changeImageToShow(null) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun TopAppBarScreen(
    currentDay: Date,
    daysList: List<Date>,
    changeCurrentDay: (Date) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val currentMonth by remember {
        derivedStateOf {
            daysList.getOrNull(scrollState.firstVisibleItemIndex)
                ?.month
                ?.convertToText()
                .orEmpty()
        }
    }

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(daysList.indexOf(currentDay))
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.background(color = AquaSpring)
    ) {
        Text(
            text = currentMonth,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600,
            fontSize = 22.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        LazyRow(
            state = scrollState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = BattleshipGrey)
                .padding(12.dp)
        ) {
            items(daysList) { day ->
                DayButton(
                    day = day,
                    isDaySelected = currentDay == day,
                    changeCurrentDay = changeCurrentDay,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

@Composable
private fun TasksField(
    displayedTasks: List<Task>,
    deleteTask: (Task) -> Unit,
    onTitleChange: (Task, String) -> Unit,
    onDescriptionChange: (Task, String) -> Unit,
    onImageClick: (String) -> Unit,
    deleteImage: (Task, String) -> Unit,
    showDeleteReminderDialog: (Task, Reminder) -> Unit,
    addImage: (Task, String) -> Unit,
    openDatePicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        itemsIndexed(displayedTasks, key = { _, task -> task.id }) { i, task ->
            TaskListItem(
                displayedTask = task,
                index = i,
                deleteTask = deleteTask,
                onTitleChange = { task, title -> onTitleChange(task, title) },
                onDescriptionChange = { task, description ->
                    onDescriptionChange(
                        task,
                        description
                    )
                },
                onImageClick = { onImageClick(it) },
                deleteImage = { task, image -> deleteImage(task, image) },
                showDeleteReminderDialog = { task, reminder ->
                    showDeleteReminderDialog(
                        task,
                        reminder
                    )
                },
                addImage = { task, image -> addImage(task, image) },
                openDatePicker = openDatePicker
            )
        }
    }
}

@Composable
private fun DateAndTimePicker(
    isShowDatePicker: Boolean,
    isShowTimePicker: Boolean,
    onDismissDate: () -> Unit,
    onDismissTime: () -> Unit,
    onDatePicked: (LocalDate) -> Unit,
    onTimePicked: (LocalTime) -> Unit,
) {
    val context = LocalContext.current
    val now = LocalDateTime.now()

    if (isShowDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                onDatePicked(LocalDate.of(year, month + 1, day))
            },
            now.year, now.monthValue - 1, now.dayOfMonth
        ).apply {
            setOnCancelListener { onDismissDate() }
            setOnDismissListener { onDismissDate() }
        }.show()
    }

    if (isShowTimePicker) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                onTimePicked(LocalTime.of(hour, minute))
            },
            now.hour, now.minute, true
        ).apply {
            setOnCancelListener { onDismissTime() }
            setOnDismissListener { onDismissTime() }
        }.show()
    }
}

@Composable
private fun DismissDeleteBox(modifier: Modifier = Modifier) {
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

@Composable
private fun DayButton(
    day: Date,
    isDaySelected: Boolean,
    changeCurrentDay: (Date) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    val borderRadius by animateDpAsState(
        targetValue = if (isDaySelected) 20.dp else 0.dp,
        animationSpec = tween(300)
    )

    Button(
        onClick = {
            focusManager.clearFocus()
            changeCurrentDay(day)
        },
        shape = RoundedCornerShape(borderRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = AquaSpring
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
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
                text = day.week.convertToText(),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun TaskListItem(
    displayedTask: Task,
    index: Int,
    deleteTask: (Task) -> Unit,
    onTitleChange: (Task, String) -> Unit,
    onDescriptionChange: (Task, String) -> Unit,
    onImageClick: (String) -> Unit,
    deleteImage: (Task, String) -> Unit,
    showDeleteReminderDialog: (Task, Reminder) -> Unit,
    addImage: (Task, String) -> Unit,
    openDatePicker: () -> Unit,
    modifier: Modifier = Modifier,
    delayToShowItem: Long = 75L,
    durationMillis: Int = 300,
) {
    Box(modifier = modifier) {
        var isVisible by rememberSaveable { mutableStateOf(false) }
        val dismissState = rememberSwipeToDismissBoxState(
            SwipeToDismissBoxValue.Settled,
            SwipeToDismissBoxDefaults.positionalThreshold
        )

        LaunchedEffect(Unit) {
            delay(delayToShowItem * index)
            isVisible = true
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(),
            exit = scaleOut(tween(durationMillis))
        ) {

            LaunchedEffect(dismissState.targetValue) {
                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                    isVisible = false
                    delay(durationMillis.toLong())
                    deleteTask(displayedTask)
                }
            }

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = { DismissDeleteBox() },
                enableDismissFromStartToEnd = false
            ) {
                TaskCard(
                    displayedTask = displayedTask,
                    onTitleChange = { task, title -> onTitleChange(task, title) },
                    onDescriptionChange = { task, description ->
                        onDescriptionChange(
                            task,
                            description
                        )
                    },
                    onImageClick = { onImageClick(it) },
                    deleteImage = { task, image -> deleteImage(task, image) },
                    showDeleteReminderDialog = { reminder ->
                        showDeleteReminderDialog(
                            displayedTask,
                            reminder
                        )
                    },
                    addImage = { task, image -> addImage(task, image) },
                    openDatePicker = openDatePicker
                )
            }
        }
    }
}

@Composable
private fun DeleteReminderDialog(
    selectedReminder: Reminder,
    deleteReminder: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AquaSpring
            ),
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Удалить уведомление на ${selectedReminder.date} ${selectedReminder.time}?",
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    onClick = { deleteReminder() },
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

@Composable
private fun TaskCard(
    displayedTask: Task,
    onTitleChange: (Task, String) -> Unit,
    onDescriptionChange: (Task, String) -> Unit,
    onImageClick: (String) -> Unit,
    deleteImage: (Task, String) -> Unit,
    showDeleteReminderDialog: (Reminder) -> Unit,
    addImage: (Task, String) -> Unit,
    openDatePicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
            .clickable { isExpanded = !isExpanded },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = BattleshipGrey)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .animateContentSize(animationSpec = tween(200))
        ) {
            TaskTitle(
                isExpanded = isExpanded,
                title = displayedTask.title,
                hide = { isExpanded = false },
                onTitleChange = { title -> onTitleChange(displayedTask, title) },
            )
            TaskMoreInfo(
                displayedTask = displayedTask,
                isExpanded = isExpanded,
                onDescriptionChange = { description ->
                    onDescriptionChange(
                        displayedTask,
                        description
                    )
                },
                onImageClick = { onImageClick(it) },
                deleteImage = { image -> deleteImage(displayedTask, image) },
                showDeleteReminderDialog = { showDeleteReminderDialog(it) },
                addImage = { image -> addImage(displayedTask, image) },
                openDatePicker = openDatePicker
            )
        }
    }
}

@Composable
private fun TaskTitle(
    title: String,
    onTitleChange: (String) -> Unit,
    isExpanded: Boolean,
    hide: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val initialValue = title

    Row(modifier = modifier) {
        if (isExpanded) {
            IconButton(
                onClick = { hide() }
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
            initialValue = initialValue,
            value = title,
            onValueChange = { onTitleChange(it) },
            enabled = isExpanded,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun TaskMoreInfo(
    displayedTask: Task,
    isExpanded: Boolean,
    onDescriptionChange: (String) -> Unit,
    onImageClick: (String) -> Unit,
    deleteImage: (String) -> Unit,
    showDeleteReminderDialog: (Reminder) -> Unit,
    addImage: (String) -> Unit,
    openDatePicker: () -> Unit,
) {
    val initialDescription = displayedTask.description

    if (isExpanded) {
        InputTextField(
            typeField = TextFieldType.DESCRIPTION_CARD,
            initialValue = initialDescription,
            value = displayedTask.description,
            onValueChange = { onDescriptionChange(it) },
            maxLines = Int.MAX_VALUE,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        )
        if (displayedTask.images.isNotEmpty()) {
            TaskImages(
                images = displayedTask.images,
                deleteImage = deleteImage,
                onImageClick = onImageClick,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (displayedTask.reminders.isNotEmpty()) {
            TaskReminders(
                reminders = displayedTask.reminders,
                showDeleteReminderDialog = { reminder -> showDeleteReminderDialog(reminder) },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        ActionButtons(
            addImage = { addImage(it) },
            openDatePicker = openDatePicker
        )
    } else if (displayedTask.description.isNotEmpty()) {
        Text(
            text = "...",
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun ActionButtons(
    addImage: (String) -> Unit,
    openDatePicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                addImage(uri.toString())
            }
        }

    Row(modifier = modifier) {
        IconButton(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.add_img),
                tint = Color.White,
                contentDescription = "Add image",
                modifier = Modifier.size(40.dp)
            )
        }
        IconButton(
            onClick = { openDatePicker() },
            modifier = Modifier.size(50.dp)
        ) {
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
}

@Composable
private fun TaskImages(
    images: List<String>,
    onImageClick: (String) -> Unit,
    deleteImage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val filesDir = context.filesDir
    LazyRow(modifier = modifier) {
        items(images) { image ->
            FileImageItem(
                fileName = image,
                filesDir = filesDir,
                onImageClick = { onImageClick(image) },
                deleteImage = { deleteImage(image) }
            )
        }
    }
}

@Composable
private fun TaskReminders(
    reminders: List<Reminder>,
    showDeleteReminderDialog: (Reminder) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier) {
        items(reminders) { reminder ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AquaSpring
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { showDeleteReminderDialog(reminder) }
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

@Composable
private fun AddFloatingActionButton(
    onCreateTaskButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = { onCreateTaskButtonClick() },
            shape = RectangleShape,
            elevation = FloatingActionButtonDefaults.elevation(16.dp),
            containerColor = FruitSalad,
            modifier = modifier
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 24.sp
            )
        }
    }
}