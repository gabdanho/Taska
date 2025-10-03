package com.example.taska.presentation.screens.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taska.domain.interfaces.repository.local.ImagesRepository
import com.example.taska.domain.interfaces.repository.local.TasksRepository
import com.example.taska.presentation.mappers.toPresentationLayer
import com.example.taska.presentation.mappers.toDomainLayer
import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.model.task.Reminder
import com.example.taska.presentation.model.task.Task
import com.example.taska.presentation.navigation.Navigator
import com.example.taska.presentation.navigation.model.AppGraph
import com.example.taska.presentation.notifications.NotificationUtils.cancelReminder
import com.example.taska.presentation.notifications.NotificationUtils.generateReminderId
import com.example.taska.presentation.notifications.NotificationUtils.isReminderInPast
import com.example.taska.presentation.notifications.NotificationUtils.scheduleReminder
import com.example.taska.presentation.utils.generateCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class TasksScreenViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val imagesRepository: ImagesRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksScreenUiState())
    val uiState: StateFlow<TasksScreenUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val displayedTasks: StateFlow<List<Task>> = _uiState
        .map { it.currentDay }
        .flatMapLatest { day ->
            tasksRepository.getTasksByDate(day.toDomainLayer())
        }
        .map { list ->
            list.map { task -> task.toPresentationLayer() }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        getDays()
        viewModelScope.launch {
            displayedTasks.collect { tasks ->
                checkAlarms(tasks)
            }
        }
    }

    fun changeCurrentDay(date: Date) {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(currentDay = date) }
        }
    }

    fun deleteTask(context: Context, task: Task) {
        viewModelScope.launch {
            task.reminders.forEach { reminder -> deleteReminder(context, task, reminder) }
            task.images.forEach { imageFile -> imagesRepository.deleteImage(imageFile) }
            tasksRepository.deleteTask(task = task.toDomainLayer())
        }
    }

    fun onTitleChange(task: Task, value: String) {
        viewModelScope.launch {
            val updatedTask = task.copy(title = value)
            tasksRepository.updateTask(task = updatedTask.toDomainLayer())
        }
    }

    fun onDescriptionChange(task: Task, value: String) {
        viewModelScope.launch {
            val updatedTask = task.copy(description = value)
            tasksRepository.updateTask(task = updatedTask.toDomainLayer())
        }
    }

    fun deleteImage(task: Task, fileName: String) {
        viewModelScope.launch {
            imagesRepository.deleteImage(fileName)
            val updatedImages = task.images - fileName
            val updatedTask = task.copy(images = updatedImages)
            tasksRepository.updateTask(task = updatedTask.toDomainLayer())
        }
    }

    fun addImage(task: Task, uriString: String) {
        viewModelScope.launch {
            val fileName = "taska_${System.currentTimeMillis()}.jpg"
            imagesRepository.saveImage(uri = uriString, fileName = fileName)

            val updatedImages = task.images + fileName
            val updatedTask = task.copy(images = updatedImages)
            tasksRepository.updateTask(task = updatedTask.toDomainLayer())
        }
    }

    fun deleteReminder(context: Context, task: Task?, reminder: Reminder?) {
        viewModelScope.launch {
            if (reminder != null && task != null) {
                cancelReminder(context, reminder.id)
                removeReminderInDb(reminder, task)
            }

            _uiState.update { state ->
                state.copy(
                    selectedTask = null,
                    selectedReminder = null
                )
            }
        }
    }

    fun onCreateTaskClick() {
        viewModelScope.launch {
            navigator.navigate(destination = AppGraph.TaskCreateScreen(date = _uiState.value.currentDay))
        }
    }

    fun changeIsShowDeleteReminderDialog(isShow: Boolean, reminder: Reminder?, task: Task?) {
        _uiState.update { state ->
            state.copy(
                isShowDeleteReminderDialog = isShow,
                selectedReminder = reminder,
                selectedTask = task
            )
        }
    }

    fun openDateAndTimePicker(selectedTask: Task, isShow: Boolean) {
        _uiState.update { state ->
            state.copy(
                isShowDatePicker = isShow,
                selectedTask = selectedTask
            )
        }
    }

    fun changeIsShowTimePicker(isShow: Boolean) {
        _uiState.update { state -> state.copy(isShowTimePicker = isShow) }
    }

    fun changeIsShowDatePicker(isShow: Boolean) {
        _uiState.update { state -> state.copy(isShowDatePicker = isShow) }
    }

    fun onDatePicked(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                selectedDate = date,
                isShowDatePicker = false,
                isShowTimePicker = true
            )
        }
    }

    fun onTimePicked(time: LocalTime) {
        _uiState.update { state -> state.copy(selectedTime = time, isShowTimePicker = false) }
    }

    fun changeImageToShow(fileName: String?) {
        _uiState.update { state -> state.copy(imageToShow = fileName) }
    }

    private fun checkAlarms(tasks: List<Task>) {
        viewModelScope.launch {
            tasks.forEach { task ->
                task.reminders.forEach { reminder ->
                    val result = isReminderInPast(
                        date = reminder.date,
                        time = reminder.time
                    )

                    if (result) removeReminderInDb(reminder, task)
                }
            }
        }
    }

    private fun getDays() {
        _uiState.update { state -> state.copy(daysList = generateCalendar()) }
    }

    fun createReminder(context: Context) {
        viewModelScope.launch {
            val task = _uiState.value.selectedTask
            val date = _uiState.value.selectedDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            val time = _uiState.value.selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm"))

            if (task != null && date != null && time != null) {
                val id = generateReminderId()
                val result = scheduleReminder(
                    context = context,
                    id = id,
                    text = task.title,
                    date = date,
                    time = time
                )

                if (result) {
                    val reminder = Reminder(
                        id = id,
                        text = task.title,
                        date = date,
                        time = time
                    )
                    saveReminderInDb(reminder, task)
                }
            }

            _uiState.update { state ->
                state.copy(
                    selectedTask = null,
                    selectedTime = null,
                    selectedDate = null
                )
            }
        }
    }

    private suspend fun saveReminderInDb(reminder: Reminder, task: Task) {
        tasksRepository.updateTask(
            task = task.copy(reminders = task.reminders + reminder).toDomainLayer()
        )
    }

    private suspend fun removeReminderInDb(reminder: Reminder, task: Task) {
        tasksRepository.updateTask(
            task = task.copy(reminders = task.reminders - reminder).toDomainLayer()
        )
    }
}