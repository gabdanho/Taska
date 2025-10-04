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
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.taska.R
import com.example.taska.presentation.theme.defaultDimensions
import java.io.File

/**
 * Элемент изображения из URI с кнопкой удаления.
 *
 * @param uri строковый uri изображения
 * @param onImageClick действие при клике по изображению
 * @param deleteImage действие при удалении
 * @param modifier модификатор
 */
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
                .size(defaultDimensions.imageSize)
                .padding(defaultDimensions.ultraSmall)
                .clickable { onImageClick() }
        )
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Delete image",
            modifier = Modifier
                .size(defaultDimensions.iconSize)
                .align(Alignment.TopEnd)
                .clickable { deleteImage() }
        )
    }
}

/**
 * Элемент изображения из файловой системы с кнопкой удаления.
 *
 * @param fileName имя файла изображения
 * @param filesDir директория файлов
 * @param onImageClick действие при клике по изображению
 * @param deleteImage действие при удалении
 * @param modifier модификатор
 */
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
            contentDescription = stringResource(R.string.content_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(defaultDimensions.imageSize)
                .padding(defaultDimensions.ultraSmall)
                .clickable { onImageClick() }
        )
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(R.string.content_delete_image),
            modifier = Modifier
                .size(defaultDimensions.iconSize)
                .align(Alignment.TopEnd)
                .clickable { deleteImage() }
        )
    }
}