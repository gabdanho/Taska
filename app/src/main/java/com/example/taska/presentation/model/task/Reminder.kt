package com.example.taska.presentation.model.task

/**
 * Модель напоминания.
 *
 * @property id идентификатор
 * @property text текст напоминания
 * @property date дата напоминания
 * @property time время напоминания
 */
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
