package com.example.taska.presentation.model.task

data class Reminder(
    val id: Int = 0,
    val text: String = "",
    val date: String = "",
    val time: String = "",
) {
    override fun toString(): String {
        return "$id|$text|$date|$time"
    }
}
