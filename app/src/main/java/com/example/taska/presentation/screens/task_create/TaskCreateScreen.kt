package com.example.taska.presentation.screens.task_create

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.taska.R
import com.example.taska.presentation.constants.TextFieldType
import com.example.taska.presentation.components.InputTextField
import com.example.taska.presentation.components.UriImageDialog
import com.example.taska.presentation.components.UriImageItem
import com.example.taska.presentation.model.task.Date
import com.example.taska.presentation.theme.AquaSqueeze
import com.example.taska.presentation.theme.BattleshipGrey
import com.example.taska.presentation.theme.FruitSalad
import com.example.taska.presentation.theme.IconTint
import com.example.taska.presentation.theme.defaultDimensions

@Composable
fun TaskCreateScreen(
    date: Date,
    modifier: Modifier = Modifier,
    viewModel: TaskCreateScreenViewModel = hiltViewModel<TaskCreateScreenViewModel>(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                viewModel.addImage(uri.toString())
            } else {
                Toast.makeText(context,
                    context.getString(R.string.text_image_is_not_choosed), Toast.LENGTH_SHORT).show()
            }
        }

    LaunchedEffect(date) {
        viewModel.getDate(date)
    }

    BackHandler {
        viewModel.onBackButtonClick()
    }

    Scaffold(
        topBar = {
            TopCreateTaskBar(
                onBackClick = { viewModel.onBackButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AquaSqueeze)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(AquaSqueeze)
                    .verticalScroll(scrollState)
            ) {
                RedactorTask(
                    title = uiState.newTask.title,
                    description = uiState.newTask.description,
                    images = uiState.imagesUris,
                    onImageClick = { viewModel.onImageClick(it) },
                    deleteImage = { viewModel.deleteImage(it) },
                    onTitleChange = { viewModel.onTitleChange(it) },
                    onDescriptionChange = { viewModel.onDescriptionChange(it) }
                )
            }

            AddImageFloatingActionButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(defaultDimensions.small)
            )
        }
    }

    uiState.imageToShow?.let { image ->
        UriImageDialog(
            imageUri = image,
            onDismiss = { viewModel.removeImageToShow() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

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
                tint = BattleshipGrey,
                contentDescription = stringResource(R.string.content_back_screen)
            )
        }
    }
}

@Composable
private fun AddImageFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RectangleShape,
        elevation = FloatingActionButtonDefaults.elevation(defaultDimensions.fabElevation),
        containerColor = FruitSalad,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.add_img),
            tint = IconTint,
            contentDescription = stringResource(R.string.content_add_image),
            modifier = Modifier.size(defaultDimensions.iconSize)
        )
    }
}

@Composable
fun RedactorTask(
    title: String,
    description: String,
    images: List<String>,
    onImageClick: (String) -> Unit,
    deleteImage: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        InputTextField(
            typeField = TextFieldType.TITLE,
            value = title,
            onValueChange = { onTitleChange(it) },
            autoFocus = true,
            modifier = Modifier
                .padding(defaultDimensions.medium)
                .fillMaxWidth()
        )
        InputTextField(
            typeField = TextFieldType.DESCRIPTION,
            value = description,
            onValueChange = { onDescriptionChange(it) },
            modifier = Modifier
                .padding(defaultDimensions.medium)
                .fillMaxWidth()
        )
        LazyRow(
            modifier = Modifier.padding(top = defaultDimensions.medium)
        ) {
            items(images) { uri ->
                UriImageItem(
                    uri = uri,
                    onImageClick = { onImageClick(uri) },
                    deleteImage = { deleteImage(uri) },
                    modifier = Modifier.size(defaultDimensions.imageSize)
                )
            }
        }
    }
}