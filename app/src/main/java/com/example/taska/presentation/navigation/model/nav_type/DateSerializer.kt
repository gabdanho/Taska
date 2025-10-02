package com.example.taska.presentation.navigation.model.nav_type

import com.example.taska.presentation.model.task.Date
import kotlinx.serialization.KSerializer

/**
 * [NavType] для передачи объектов [UserLogin] между экранами.
 */
class DateSerializerNavType(serializer: KSerializer<Date> = Date.serializer()) :
    NavTypeSerializer<Date>(
        serializer = serializer
    )