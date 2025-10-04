package com.example.taska.data.local.model

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
)