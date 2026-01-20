package com.example.mycalendar.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/* ---------- LIGHT COLORS ---------- */

// Основной акцент (кнопки, FAB, выбранные дни)
val LightPrimary = Color(0xFF4F6BED)

// Контейнеры акцента (выделенные карточки, header)
val LightPrimaryContainer = Color(0xFFE8ECFF)

// Успех / выполнено
val LightSecondary = Color(0xFF4CAF50)
val LightSecondaryContainer = Color(0xFFE8F5E9)

// Фон экрана
val LightBackground = Color(0xFFF7F8FA)

// Карточки, списки
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F3F6)

// Основной текст
val LightOnBackground = Color(0xFF1C1F26)
val LightOnSurface = Color(0xFF1C1F26)

// Вторичный текст
val LightOnSurfaceVariant = Color(0xFF6B7280)

// Разделители, бордеры
val LightOutline = Color(0xFFE0E3EB)

// Ошибки
val LightError = Color(0xFFE53935)

/* ---------- DARK COLORS ---------- */
// Основной акцент
val DarkPrimary = Color(0xFF8FA3FF)

// Контейнеры акцента
val DarkPrimaryContainer = Color(0xFF2A335F)

// Успех / выполнено
val DarkSecondary = Color(0xFF81C784)
val DarkSecondaryContainer = Color(0xFF1E3A2B)

// Фон экрана (НЕ чёрный)
val DarkBackground = Color(0xFF121826)

// Карточки
val DarkSurface = Color(0xFF1A2032)
val DarkSurfaceVariant = Color(0xFF232A3F)

// Основной текст
val DarkOnBackground = Color(0xFFEAEAF0)
val DarkOnSurface = Color(0xFFEAEAF0)

// Вторичный текст
val DarkOnSurfaceVariant = Color(0xFFA1A1AA)

// Разделители
val DarkOutline = Color(0xFF2F3654)

// Ошибки
val DarkError = Color(0xFFEF5350)

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    primaryContainer = LightPrimaryContainer,
    secondary = LightSecondary,
    secondaryContainer = LightSecondaryContainer,

    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,

    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,

    outline = LightOutline,
    error = LightError
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryContainer,
    secondary = DarkSecondary,
    secondaryContainer = DarkSecondaryContainer,

    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,

    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,

    outline = DarkOutline,
    error = DarkError
)

@Composable
fun MyCalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}