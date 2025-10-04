package com.example.taska.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Типы текстовых полей с настройками шрифта.
 *
 * @param fontSize размер текста
 * @param textResName ресурс плейсхолдера
 * @param textColor цвет текста
 * @param fontWeight жирность текста
 */
enum class TextFieldType(
    val fontSize: Int,
    val textResName: StringResNamePresentation? = null,
    val textColor: Color = Color.Companion.Black,
    val fontWeight: FontWeight = FontWeight.Companion.Normal
) {
    TITLE(36, StringResNamePresentation.TEXT_TITLE, fontWeight = FontWeight.Companion.W400),
    DESCRIPTION(20, StringResNamePresentation.TEXT_DESCRIPTION),
    DEFAULT(16),
    TITLE_CARD(18, StringResNamePresentation.TEXT_TITLE, Color.Companion.White, FontWeight.Companion.W400),
    DESCRIPTION_CARD(15, StringResNamePresentation.TEXT_DESCRIPTION, Color.Companion.White)
}