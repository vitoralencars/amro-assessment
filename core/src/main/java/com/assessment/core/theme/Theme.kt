package com.assessment.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkRed,
    primaryContainer = Graffiti,
    secondaryContainer = LightGrey,
    tertiaryContainer = PositiveGreen,
    onBackground = White,
    onPrimary = White,
    onSecondaryContainer = DarkGrey,
    onTertiaryContainer = LightGrey,
    secondary = DarkRed,
    tertiary = Black,
    inverseOnSurface = White,
    onSurface = MediumGrey,
    onSurfaceVariant = White,
    surfaceContainerLow = DarkYellow,
    background = Black,
    surface = Black,
    inversePrimary = White,
    outline = LightGrey,
)

private val LightColorScheme = lightColorScheme(
    primary = DarkRed,
    primaryContainer = White,
    secondaryContainer = LightGrey,
    tertiaryContainer = PositiveGreen,
    onBackground = Black,
    onPrimary = White,
    onSecondaryContainer = DarkGrey,
    onTertiaryContainer = LightGrey,
    secondary = Black,
    tertiary = Black,
    background = SmokedWhite,
    surface = White,
    inverseOnSurface = White,
    onSurfaceVariant = Black,
    surfaceContainerLow = DarkYellow,
    onSurface = DarkGrey,
    inversePrimary = White,
    outline = MediumGrey,
)

@Composable
fun AmroTheme(
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
