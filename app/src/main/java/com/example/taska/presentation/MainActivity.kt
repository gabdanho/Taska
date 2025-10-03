package com.example.taska.presentation

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import com.example.taska.presentation.screens.main.MainScreen
import com.example.taska.presentation.theme.TaskaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    this,
                    "Нет разрешения на получение уведомлений",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        setContent {
            TaskaTheme {
                MainScreen()
            }
        }
    }

    private fun checkPermissions() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && !NotificationManagerCompat.from(this).areNotificationsEnabled()
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            && !alarmManager.canScheduleExactAlarms()
        ) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }
}