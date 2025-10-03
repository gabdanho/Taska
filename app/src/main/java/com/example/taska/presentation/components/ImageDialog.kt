package com.example.taska.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.AsyncImage
import java.io.File

@Composable
fun FileImageDialog(
    image: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        val context = LocalContext.current
        val file = File(context.filesDir, image)

        AsyncImage(
            model = file,
            contentDescription = "Selected image",
            modifier = modifier
        )
    }
}

@Composable
fun UriImageDialog(
    imageUri: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        val uri = imageUri.toUri()

        AsyncImage(
            model = uri,
            contentDescription = "Selected image",
            modifier = modifier
        )
    }
}