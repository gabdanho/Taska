package com.example.taska.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val emptyDp: Dp,
    val ultraSmall: Dp,
    val verySmall: Dp,
    val small: Dp,
    val medium: Dp,
    val big: Dp,
    val imageSize: Dp,
    val iconSize: Dp,
    val fabElevation: Dp,
    val dayButtonSize: Dp,
    val borderDayButtonDp: Dp,
    val actionButtonSize: Dp,
)

val defaultDimensions = Dimensions(
    emptyDp = 0.dp,
    ultraSmall = 2.dp,
    verySmall = 6.dp,
    small = 8.dp,
    medium = 12.dp,
    big = 16.dp,
    imageSize = 300.dp,
    iconSize = 30.dp,
    fabElevation = 16.dp,
    dayButtonSize = 60.dp,
    borderDayButtonDp = 20.dp,
    actionButtonSize = 50.dp,
)