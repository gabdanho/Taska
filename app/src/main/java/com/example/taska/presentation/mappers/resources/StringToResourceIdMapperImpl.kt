package com.example.taska.presentation.mappers.resources

import com.example.taska.R
import com.example.taska.presentation.model.StringResNamePresentation

/**
 * Реализация маппера, которая сопоставляет все возможные
 * идентификаторы из presentation слоя с конкретными ресурсами строк в R.string.
 * Используется для отображения локализованных текстов в UI.
 */
class StringToResourceIdMapperImpl : StringToResourceIdMapper {
    private val resourceMapPresentation = mapOf(
        StringResNamePresentation.TEXT_JANUARY to R.string.text_january,
        StringResNamePresentation.TEXT_FEBRUARY to R.string.text_february,
        StringResNamePresentation.TEXT_MARCH to R.string.text_march,
        StringResNamePresentation.TEXT_APRIL to R.string.text_april,
        StringResNamePresentation.TEXT_MAY to R.string.text_may,
        StringResNamePresentation.TEXT_JUNE to R.string.text_june,
        StringResNamePresentation.TEXT_JULY to R.string.text_july,
        StringResNamePresentation.TEXT_AUGUST to R.string.text_august,
        StringResNamePresentation.TEXT_SEPTEMBER to R.string.text_september,
        StringResNamePresentation.TEXT_OCTOBER to R.string.text_october,
        StringResNamePresentation.TEXT_NOVEMBER to R.string.text_november,
        StringResNamePresentation.TEXT_DECEMBER to R.string.text_december,

        StringResNamePresentation.TEXT_MONDAY to R.string.text_monday,
        StringResNamePresentation.TEXT_TUESDAY to R.string.text_tuesday,
        StringResNamePresentation.TEXT_WEDNESDAY to R.string.text_wednesday,
        StringResNamePresentation.TEXT_THURSDAY to R.string.text_thursday,
        StringResNamePresentation.TEXT_FRIDAY to R.string.text_friday,
        StringResNamePresentation.TEXT_SATURDAY to R.string.text_saturday,
        StringResNamePresentation.TEXT_SUNDAY to R.string.text_sunday,

        StringResNamePresentation.TEXT_TITLE to R.string.text_task_title,
        StringResNamePresentation.TEXT_DESCRIPTION to R.string.text_task_description
    )

    override fun map(resId: StringResNamePresentation): Int {
        return resourceMapPresentation[resId]
            ?: throw IllegalArgumentException("CANT_MAP_THIS_ID_$resId")
    }
}