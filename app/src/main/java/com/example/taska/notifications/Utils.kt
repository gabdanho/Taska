package com.example.taska.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

object Utils {
    fun getPendingIntent(context: Context, id: Int, text: String): PendingIntent {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("text", text)
            putExtra("id", id)
        }
        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    fun addZero(count: Int): String {
        return if (count < 10) "0$count" else count.toString()
    }

    fun getCurrentDay(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(currentDate)
    }

    fun getId(): Int {
        return (Math.random() * 1000000).roundToInt()
    }

    fun isReminderInPast(date: String, time: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val reminderDateTime = LocalDateTime.parse("$date $time", formatter)
        val now = LocalDateTime.now(ZoneId.systemDefault())
        return reminderDateTime.isBefore(now)
    }
}