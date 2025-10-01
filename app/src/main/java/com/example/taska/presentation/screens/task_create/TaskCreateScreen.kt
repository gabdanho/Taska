package com.example.taska.presentation.screens.task_create

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.taska.presentation.constants.TextFieldType
import com.example.taska.presentation.components.InputTextField
import com.example.taska.presentation.theme.AquaSqueeze
import com.example.taska.presentation.theme.FruitSalad

@Composable
fun TaskCreateScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                addImage(uri)
            } else {
                Toast.makeText(context, "Изображение не было выбрано", Toast.LENGTH_SHORT).show()
            }
        }

    BackHandler {
        onBackClick()
    }

    Scaffold(
        topBar = {
            TopCreateTaskBar(
                onBackClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AquaSqueeze)
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AquaSqueeze)
        ) {
            RedactorTask(
                title = newTask.title,
                description = newTask.description,
                images = newTask.images,
                onImageClick = { onImageClick(it) },
                deleteImage = { deleteImage(it) },
                onTitleChange = { onTitleChange(it) },
                onDescriptionChange = { onDescriptionChange(it) }
            )
        }
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            AddImageFloatingActionButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    if (image != null) {
        Dialog(onDismissRequest = { removeImageToShow() }) {
            AsyncImage(
                model = image,
                contentDescription = "Selected image",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// +++
@Composable
private fun TopCreateTaskBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        IconButton(
            onClick = { onBackClick() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back screen"
            )
        }
    }
}

// +++
@Composable
private fun ImageItem(
    uri: Uri,
    onImageClick: (Uri) -> Unit,
    deleteImage: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(300.dp)
                .padding(2.dp)
                .clickable { onImageClick(uri) }
        )
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Delete image",
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    deleteImage(uri)
                }
        )
    }
}

// +++
@Composable
private fun AddImageFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RectangleShape,
        elevation = FloatingActionButtonDefaults.elevation(16.dp),
        containerColor = FruitSalad,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.add_img),
            tint = Color.White,
            contentDescription = "Add image",
            modifier = Modifier.size(40.dp)
        )
    }
}

// +++
@Composable
fun RedactorTask(
    title: String,
    description: String,
    images: List<Uri>,
    onImageClick: (Uri) -> Unit,
    deleteImage: (Uri) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
) {
    val initialTitle = title
    val initialDescription = description

    InputTextField(
        typeField = TextFieldType.TITLE,
        value = title,
        onValueChange = { onTitleChange(it) },
        autoFocus = true,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        initialValue = initialTitle
    )
    InputTextField(
        typeField = TextFieldType.DESCRIPTION,
        value = description,
        onValueChange = { onDescriptionChange(it) },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        initialValue = initialDescription
    )
    LazyRow(
        modifier = Modifier.padding(top = 12.dp)
    ) {
        items(images) { uri ->
            ImageItem(
                uri = uri,
                onImageClick = { onImageClick(it) },
                deleteImage = { deleteImage(it) },
                modifier = Modifier.size(300.dp)
            )
        }
    }
}