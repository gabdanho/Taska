package com.example.taska.presentation.screens.task_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taska.domain.interfaces.repository.local.TasksRepository
import com.example.taska.presentation.mappers.toDomainLayer
import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskCreateScreenViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskCreateScreenUiState())
    val uiState: StateFlow<TaskCreateScreenUiState> = _uiState.asStateFlow()

    fun addImage(uriString: String) {
        val newImages = _uiState.value.newTask.images + uriString
        val updateNewTask = _uiState.value.newTask.copy(images = newImages)

        _uiState.update { state -> state.copy(newTask = updateNewTask) }
    }

    fun onBackButtonClick() {
        viewModelScope.launch {
            createTask()
            navigator.navigatePopBackStack()
        }
    }

    fun deleteImage(uriString: String) {
        val newImages = _uiState.value.newTask.images - uriString
        val updateNewTask = _uiState.value.newTask.copy(images = newImages)

        _uiState.update { state -> state.copy(newTask = updateNewTask) }
    }

    fun onTitleChange(value: String) {
        val updateNewTask = _uiState.value.newTask.copy(title = value)
        _uiState.update { state -> state.copy(newTask = updateNewTask) }
    }

    fun onDescriptionChange(value: String) {
        val updateNewTask = _uiState.value.newTask.copy(description = value)
        _uiState.update { state -> state.copy(newTask = updateNewTask) }
    }

    fun onImageClick(uriString: String) {
        _uiState.update { state -> state.copy(imageToShow = uriString) }
    }

    fun removeImageToShow() {
        _uiState.update { state -> state.copy(imageToShow = null) }
    }

    fun getDate(date: Date) {
        _uiState.update { state -> state.copy(currentDate = date) }
    }

    private fun createTask() {
        if (isCanCreateTask()) {
            viewModelScope.launch {
                _uiState.value.currentDate?.let { date ->
                    val formattedTask = _uiState.value.newTask.copy(date = date)
                    tasksRepository.addTask(task = formattedTask.toDomainLayer())
                }
            }
        }
    }

    private fun isCanCreateTask(): Boolean {
        val state = _uiState.value
        return when {
            state.newTask.title.isBlank() && state.newTask.description.isBlank() -> false

            state.newTask.title.isNotBlank() -> true

            _uiState.value.newTask.description.isNotBlank() -> {
                createTitleFromDescription()
                true
            }

            else -> false
        }
    }

    private fun createTitleFromDescription() {
        val newTitle =
            _uiState.value.newTask.description
                .split(" ")
                .take(DESCRIPTION_WORDS)
                .joinToString(" ")
        _uiState.update { state -> state.copy(newTask = state.newTask.copy(title = newTitle)) }
    }

    companion object {
        private const val DESCRIPTION_WORDS = 5
    }
}