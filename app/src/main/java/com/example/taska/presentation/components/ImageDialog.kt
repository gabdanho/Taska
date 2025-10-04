package com.example.taska.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.taska.R
import java.io.File

/**
 * Диалог отображения изображения из файла.
 *
 * @param image имя файла изображения
 * @param onDismiss колбэк закрытия диалога
 * @param modifier модификатор
 */
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
            contentDescription = stringResource(R.string.content_selected_image),
            modifier = modifier
        )
    }
}

/**
 * Диалог отображения изображения из URI.
 *
 * @param imageUri строковый uri изображения
 * @param onDismiss колбэк закрытия диалога
 * @param modifier модификатор
 */
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
            contentDescription = stringResource(R.string.content_selected_image),
            modifier = modifier
        )
    }
}