package com.example.taska.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.example.taska.presentation.navigation.model.NavigationDestination
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс навигационного контроллера.
 *
 * Позволяет:
 * - Навигацию к конкретному [com.example.taska.presentation.navigation.model.NavigationDestination]
 * - Навигацию назад
 * - Подписку на поток навигационных событий
 */
interface Navigator {

    /** Стартовый экран приложения */
    val startDestination: NavigationDestination

    /** Поток событий навигации */
    val navigationActions: Flow<NavigationAction>

    /** Перейти на экран [destination] с опциональными [navOptions] */
    suspend fun navigate(
        destination: NavigationDestination,
        navOptions: NavOptionsBuilder.() -> Unit = {}
    )

    /** Вернуться назад в стеке навигации */
    suspend fun navigatePopBackStack()
}