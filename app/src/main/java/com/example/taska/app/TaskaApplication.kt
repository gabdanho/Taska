package com.example.taska.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply {
                    description = channelDescription
                    setSound(
                        Settings.System.DEFAULT_NOTIFICATION_URI, AudioAttributes.Builder().setUsage(
                            AudioAttributes.USAGE_NOTIFICATION).build())
                }
            val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val channelName = "Taska"
        const val channelDescription = "Уведомления для напоминания выполнить задачу"
        const val channelId = "taskas"
    }
}