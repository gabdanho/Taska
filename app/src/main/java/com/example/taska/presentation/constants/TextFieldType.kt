package com.example.taska.presentation.constants

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.taska.presentation.model.StringResNamePresentation

enum class TextFieldType(
    val fontSize: Int,
    val textResName: StringResNamePresentation? = null,
    val textColor: Color = Color.Black,
    val fontWeight: FontWeight = FontWeight.Normal
) {
    TITLE(36, StringResNamePresentation.TEXT_TITLE, fontWeight = FontWeight.W400),
    DESCRIPTION(20, StringResNamePresentation.TEXT_DESCRIPTION),
    DEFAULT(16),
    TITLE_CARD(18, StringResNamePresentation.TEXT_TITLE, Color.White, FontWeight.W400),
    DESCRIPTION_CARD(15, StringResNamePresentation.TEXT_DESCRIPTION, Color.White)
}