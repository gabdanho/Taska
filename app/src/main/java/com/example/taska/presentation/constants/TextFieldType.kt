package com.example.taska.presentation.constants

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

enum class TextFieldType(
    val fontSize: Int,
    val text: String = "",
    val textColor: Color = Color.Black,
    val fontWeight: FontWeight = FontWeight.Normal
) {
    TITLE(36, "Название", fontWeight = FontWeight.W400),
    DESCRIPTION(20, "Описание"),
    DEFAULT(16),
    TITLE_CARD(18, "Название", Color.White, FontWeight.W400),
    DESCRIPTION_CARD(15, "Описание", Color.White)
}