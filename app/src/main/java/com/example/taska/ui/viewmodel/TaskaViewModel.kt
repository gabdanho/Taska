package com.example.taska.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taska.data.Task
import com.example.taska.data.TaskDao
import com.example.taska.model.date.Day
import com.example.taska.model.date.generateCalendar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskaViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskaUiState())
    val uiState: StateFlow<TaskaUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDays()
            refreshTasks()
            delay(1000)
            _uiState.update { state ->
                state.copy(isDataLoaded = true)
            }
        }
    }

    private fun getDays() {
        _uiState.update { state ->
            state.copy(daysList = generateCalendar())
        }
    }

    private fun refreshTasks(day: Day = uiState.value.currentDay) {
        viewModelScope.launch {
            taskDao.getTasksByDate(day).collect { tasks ->
                _uiState.update { state ->
                    state.copy(currentTasks = tasks)
                }
            }
        }
    }

    suspend fun createTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.addTask(task)
            delay(50)
            refreshTasks()
        }
    }

    fun changeCurrentDay(day: Day) {
        if (day != uiState.value.currentDay) {
            _uiState.update { state ->
                state.copy(
                    currentDay = day
                )
            }
            refreshTasks(day)
        }
    }

    suspend fun removeTaskFromDatabase(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }

    fun changeRefreshState(flag: Boolean) {
        _uiState.update { state ->
            state.copy(isCanRefreshTasks = flag)
        }
    }
}