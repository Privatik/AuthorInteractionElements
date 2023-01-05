package com.io.core.ui

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val defaultColors: Colors = Colors(
    primary = DefaultColor,
    primaryVariant = DefaultColor,
    secondary = DefaultColor,
    secondaryVariant = DefaultColor,
    background = DefaultColor,
    surface = DefaultColor,
    error = DefaultColor,
    onPrimary = DefaultColor,
    onSecondary = DefaultColor,
    onBackground = DefaultColor,
    onSurface = DefaultColor,
    onError = DefaultColor,
    isLight = false
)

@Composable
fun Theme(content: @Composable () -> Unit) {

    MaterialTheme(
        colors = defaultColors,
        typography = Typography,
        shapes = Shapes
    ) {
        CompositionLocalProvider(
            LocalPaletteColors provides DarkPalette,
            LocalDimens provides DefaultDimens,
            content = content
        )
    }
}