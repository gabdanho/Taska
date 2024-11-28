package com.example.taska.ui.viewmodel

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taska.data.Task
import com.example.taska.data.TaskDao
import com.example.taska.model.date.Day
import com.example.taska.model.date.generateCalendar
import com.example.taska.notifications.Reminder
import com.example.taska.notifications.Utils.addZero
import com.example.taska.notifications.Utils.getCurrentDay
import com.example.taska.notifications.Utils.getId
import com.example.taska.notifications.Utils.getPendingIntent
import com.example.taska.notifications.Utils.isReminderInPast
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TaskaViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
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
                    val tasksWithoutPastReminders = tasks.map { task ->
                        task.copy(
                            reminders = task.reminders.filterNot { reminder ->
                                isReminderInPast(reminder.date, reminder.time).also { isPast ->
                                    if (isPast) removeReminder(task, reminder, context)
                                }
                            }
                        )
                    }
                    state.copy(currentTasks = tasksWithoutPastReminders)
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

    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.updateTask(task)
        }
    }

    fun changeCurrentDay(day: Day) {
        if (day != uiState.value.currentDay) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _uiState.update { state ->
                        state.copy(
                            currentDay = day
                        )
                    }
                    refreshTasks(day)
                }
            }
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

    fun getLastTaskId(): Int = uiState.value.currentTasks.lastOrNull()?.id ?: 0

    fun addReminderForTaska(context: Context, task: Task): Reminder? {
        val taskReminders = task.reminders.toMutableList()

        if (uiState.value.date.isEmpty() && uiState.value.time.isEmpty()) {
            Toast.makeText(context, "Не выбрана дата и время уведомления", Toast.LENGTH_SHORT).show()
            return null
        } else if (uiState.value.text.isEmpty()) {
            Toast.makeText(context, "Не выбрано напоминание", Toast.LENGTH_SHORT).show()
            return null
        }

        if (uiState.value.time.isEmpty()) _uiState.update { it.copy(time = "12:00") }
        if (uiState.value.date.isEmpty()) _uiState.update { it.copy(date = getCurrentDay()) }

        val reminder = Reminder(getId(), uiState.value.text, uiState.value.date, uiState.value.time)
        taskReminders.add(reminder)

        val updatedTask = task.copy(reminders = taskReminders)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(updatedTask)
            }
        }
        scheduleNotification(context, reminder.date, reminder.time, reminder.text, reminder.id)
        _uiState.update { it.copy(time = "", date = "", text = "") }

        return reminder
    }

    fun removeReminder(task: Task, reminder: Reminder, context: Context) {
        val updatedTaskReminder = task.reminders.toMutableList().apply {
            remove(reminder)
        }
        val updatedTask = task.copy(reminders = updatedTaskReminder)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateTask(updatedTask)
            }
        }
        alarmManager.cancel(getPendingIntent(context, reminder.id, reminder.text))
    }

    private fun scheduleNotification(context: Context, date: String, time: String, text: String, id: Int) {
        val dateTime = "$date $time"
        println(dateTime)
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val triggerTime = sdf.parse(dateTime)?.time ?: return
        val pendingIntent = getPendingIntent(context, id, text)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }

    fun changeReminderText(text: String) {
        _uiState.update { it.copy(text = text) }
    }

    fun changeDate(year: Int, month: Int, day: Int) {
        _uiState.update { it.copy(date = "${addZero(day)}.${addZero(month + 1)}.$year") }
    }

    fun changeTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(time = "${addZero(hour)}:${addZero(minute)}") }
    }
}