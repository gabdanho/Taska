package com.example.taska.presentation.screens.task_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taska.domain.interfaces.repository.local.TasksRepository
import com.example.taska.presentation.mappers.toDomainLayer
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskCreateScreenUiState())
    val uiState: StateFlow<TaskCreateScreenUiState> = _uiState.asStateFlow()

    fun addImage(uriString: String) {
        val newImages = _uiState.value.newTask.images + uriString
        val updateNewTask = _uiState.value.newTask.copy(images = newImages)

        _uiState.update { state -> state.copy(newTask = updateNewTask) }
    }

    fun onBackButtonClick() {
        createTask()
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

    private fun createTask() {
        viewModelScope.launch {
            tasksRepository.addTask(
                task = _uiState.value.newTask.toDomainLayer()
            )
        }
    }
}