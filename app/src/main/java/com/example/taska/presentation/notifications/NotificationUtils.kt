package com.example.taska.presentation.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import kotlin.math.roundToInt

private const val DATE_TIME_FORMATTER = "dd.MM.yyyy HH:mm"

/**
 * Утилиты для работы с уведомлениями и напоминаниями.
 */
object NotificationUtils {

    /**
     * Запланировать напоминание.
     *
     * @param context контекст
     * @param id id напоминания
     * @param text текст уведомления
     * @param date дата напоминания (строка)
     * @param time время напоминания (строка)
     */
    fun scheduleReminder(context: Context, id: Int, text: String, date: String, time: String): Boolean {
        try {
            val formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)
            val dateTime = LocalDateTime.parse("$date $time", formatter)
            val epochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val pendingIntent = getPendingIntent(context, id, text)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                return false
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, epochMillis, pendingIntent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Отменить напоминание.
     *
     * @param context контекст
     * @param id id напоминания
     */
    fun cancelReminder(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = getPendingIntent(context, id, "")
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Создать PendingIntent для уведомления.
     *
     * @param context контекст
     * @param id id напоминания
     * @param text текст уведомления
     */
    fun getPendingIntent(context: Context, id: Int, text: String): PendingIntent {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("text", text)
            putExtra("id", id)
        }
        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    /** Сгенерировать случайный id напоминания. */
    fun generateReminderId(): Int {
        return (Math.random() * 1000000).roundToInt()
    }

    /**
     * Проверить, что дата/время напоминания в прошлом.
     *
     * @param date дата (строка)
     * @param time время (строка)
     */
    fun isReminderInPast(date: String, time: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)
        val reminderDateTime = LocalDateTime.parse("$date $time", formatter)
        val now = LocalDateTime.now(ZoneId.systemDefault())
        return reminderDateTime.isBefore(now)
    }
}