package com.rebine.chocostock.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ChocoStockColors = lightColorScheme(
    primary = Cocoa,
    onPrimary = Cream,
    secondary = Praline,
    onSecondary = Cream,
    tertiary = Gold,
    onTertiary = Cocoa,
    background = Cream,
    onBackground = Cocoa,
    surface = DarkCream,
    onSurface = Cocoa,
    onSurfaceVariant = WarmInk,
    error = CherryRed,
    onError = Cream,

    primaryContainer = Praline,
    onPrimaryContainer = Cream,
)

@Composable
fun ChocoStockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Don't use dynamic color (Material You) to keep our color scheme
    val colorScheme = ChocoStockColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ChocoStockTypography,
        content = content
    )
}
