package com.example.taska.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import java.io.File

@Composable
fun UriImageItem(
    uri: String,
    onImageClick: () -> Unit,
    deleteImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = uri.toUri(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(300.dp)
                .padding(2.dp)
                .clickable { onImageClick() }
        )
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Delete image",
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopEnd)
                .clickable { deleteImage() }
        )
    }
}

@Composable
fun FileImageItem(
    fileName: String,
    filesDir: File,
    onImageClick: () -> Unit,
    deleteImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val file = File(filesDir, fileName)
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = file,
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(300.dp)
                .padding(2.dp)
                .clickable { onImageClick() }
        )
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Delete image",
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopEnd)
                .clickable { deleteImage() }
        )
    }
}