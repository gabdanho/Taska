package com.example.taska.presentation.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taska.domain.interfaces.repository.local.TasksRepository
import com.example.taska.presentation.mappers.toDomainLayer
import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.model.task.Reminder
import com.example.taska.presentation.model.task.Task
import com.example.taska.presentation.utils.generateCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TasksScreenViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksScreenUiState())
    val uiState: StateFlow<TasksScreenUiState> = _uiState.asStateFlow()

    init {
        getDays()
    }

    fun changeCurrentDay(date: Date) {
        _uiState.update { state -> state.copy(currentDay = date) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
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

    fun deleteImage(task: Task, uriString: String) {
        viewModelScope.launch {
            val updatedImages = task.images - uriString
            val updatedTask = task.copy(images = updatedImages)
            tasksRepository.updateTask(task = updatedTask.toDomainLayer())
        }
    }

    fun addImage(task: Task, uriString: String) {
        viewModelScope.launch {
            val updatedImages = task.images + uriString
            val updatedTask = task.copy(images = updatedImages)
            tasksRepository.updateTask(task = updatedTask.toDomainLayer())
        }
    }

    fun deleteReminder() {
        viewModelScope.launch {
            val state = _uiState.value
            state.selectedReminder?.let { deletedReminder ->
                state.selectedTask?.let { task ->
                    val updatedReminders = task.reminders - deletedReminder
                    val updatedTask = task.copy(reminders = updatedReminders)
                    tasksRepository.updateTask(task = updatedTask.toDomainLayer())
                }
            }
            _uiState.update { state -> state.copy(isShowDeleteReminderDialog = false) }
        }
    }

    fun onCreateTaskClick() {
        // TODO : Навигация
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

    fun changeIsShowDatePicker(isShow: Boolean) {
        _uiState.update { state -> state.copy(isShowDatePicker = isShow) }
        changeIsShowDateTimePicker(isShow = isShow)
    }

    fun changeIsShowTimePicker(isShow: Boolean) {
        _uiState.update { state -> state.copy(isShowTimePicker = isShow) }
    }

    fun changeIsShowDateTimePicker(isShow: Boolean) {
        _uiState.update { state -> state.copy(isShowDateTimePicker = isShow) }
    }

    fun onDatePicked(date: LocalDate) {
        _uiState.update { state -> state.copy(selectedDate = date) }
        changeIsShowDatePicker(isShow = false)
        changeIsShowTimePicker(isShow = true)
    }

    fun onTimePicked(time: LocalTime) {
        _uiState.update { state -> state.copy(selectedTime = time) }
        changeIsShowTimePicker(false)
        changeIsShowDateTimePicker(false)
    }

    fun changeImageToShow(uriString: String?) {
        _uiState.update { state -> state.copy(imageToShow = uriString) }
    }

    private fun getDays() {
        _uiState.update { state -> state.copy(daysList = generateCalendar()) }
    }
}