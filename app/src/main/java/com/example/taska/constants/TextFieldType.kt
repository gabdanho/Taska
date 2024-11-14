package com.example.taska.constants

enum class TextFieldType(val fontSize: Int, val text: String = "") {
    TITLE(36, "Название"),
    DESCRIPTION(20, "Описание"),
    DEFAULT(16)
}