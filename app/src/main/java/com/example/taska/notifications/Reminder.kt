package com.example.taska.notifications

data class Reminder(
    val id: Int,
    val text: String,
    val date: String,
    val time: String
) {
    override fun toString(): String {
        return "$id|$text|$date|$time"
    }
}
