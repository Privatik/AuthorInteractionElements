package com.io.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

object ProjectTheme {

    val palette: Palette
        @Composable
        @ReadOnlyComposable
        get() = LocalPaletteColors.current

    val dimens: Dimens
        @Composable
        @ReadOnlyComposable
        get() = LocalDimens.current
}

val LocalPaletteColors = compositionLocalOf<Palette> {
    error("No LocalPaletteColors provided")
}

val LocalDimens = compositionLocalOf<Dimens> {
    error("No LocalDimens provided")
}