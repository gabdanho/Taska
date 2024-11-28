package com.example.taska.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.taska.R
import com.example.taska.constants.TextFieldType
import com.example.taska.data.Task
import com.example.taska.model.date.Day
import com.example.taska.ui.custom.InputTextField
import com.example.taska.ui.theme.AquaSqueeze
import com.example.taska.ui.theme.FruitSalad
import com.example.taska.utils.ImageManager.saveImageToInternalStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun TaskCreateScreen(
    day: Day,
    getLastTaskId: () -> Int,
    toBackScreen: () -> Unit,
    changeRefreshState: (Boolean) -> Unit,
    createTask: suspend (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var task by remember { mutableStateOf(Task(date = day)) }
    var titleValue by remember { mutableStateOf("") }
    var descriptionValue by remember { mutableStateOf("") }

    val imagesUrls = remember { mutableStateListOf<String>() }
    val tempImagesUri = remember { mutableStateListOf<Uri>() }
    var imageId by remember { mutableStateOf(1) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            tempImagesUri.add(uri)
        } else {
            Toast.makeText(context, "Изображение не было выбрано", Toast.LENGTH_SHORT).show()
        }
    }
    var showImage by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val taskFormation = {
        task.copy(
            title = if (titleValue == "") descriptionValue.split(" ").take(3).joinToString(" ") else titleValue,
            description = descriptionValue,
            imagesId = imagesUrls.toList()
        )
    }
    val toBackButtonClick = {
        if (titleValue == "" && descriptionValue == "") {
            Toast.makeText(context, "Пустая заметка не была добавлена", Toast.LENGTH_SHORT).show()
            changeRefreshState(true)
            toBackScreen()
        } else {
            if (tempImagesUri.isNotEmpty()) {
                tempImagesUri.forEach { uri ->
                    val fileName = "${getLastTaskId() + 1}-${imageId}.jpg"
                    saveImageToInternalStorage(context, uri, fileName)
                    val file = File(context.filesDir, fileName)
                    if (file.exists()) {
                        imagesUrls.add(fileName)
                        imageId++
                    } else {
                        Toast.makeText(context, "Ошибка: файл $fileName не найден", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            task = taskFormation()
            coroutineScope.launch {
                createTask(task)
                changeRefreshState(true)
                delay(50)
                toBackScreen()
            }
        }
    }

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                toBackButtonClick()
                this.remove()
            }
        }
    }

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    backDispatcher?.addCallback(backCallback)

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AquaSqueeze)
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
            ) {
                IconButton(
                    onClick = {
                        toBackButtonClick()
                        backCallback.remove()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back screen"
                    )
                }
            }
        }
    ) { innerPadding ->
        if (showImage) {
            Dialog(onDismissRequest = { showImage = false }) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected image",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AquaSqueeze)
        ) {
            InputTextField(
                typeField = TextFieldType.TITLE,
                value = titleValue,
                onValueChange = { titleValue = it },
                autoFocus = true,
                modifier = Modifier.padding(12.dp),
                initialValue = task.title
            )
            InputTextField(
                typeField = TextFieldType.DESCRIPTION,
                value = descriptionValue,
                onValueChange = { descriptionValue = it },
                modifier = Modifier.padding(12.dp),
                initialValue = task.title
            )
            LazyRow(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                items(tempImagesUri) { uri ->
                    Box(
                        modifier = Modifier.size(300.dp)
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = "Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(300.dp)
                                .padding(2.dp)
                                .clickable {
                                    selectedImageUri = uri
                                    showImage = true
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Delete image",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    tempImagesUri.remove(uri)
                                    backCallback.remove()
                                }
                        )
                    }
                }
            }
        }
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            FloatingActionButton(
                onClick = {
                    galleryLauncher.launch("image/*")
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
                Icon(
                    painter = painterResource(R.drawable.add_img),
                    tint = Color.White,
                    contentDescription = "Add image",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

//@Preview
//@Composable
//private fun TaskCreateScreenPreview() {
//    TaskCreateScreen(
//        day = Day(
//            month = Month.NOVEMBER,
//            number = 14,
//            year = 2024,
//            week = DayOfWeek.FRIDAY
//        ),
//        onCreateTaskClick = { }
//    )
//}