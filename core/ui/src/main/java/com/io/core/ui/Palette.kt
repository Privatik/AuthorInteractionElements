package com.io.core.ui

import androidx.compose.ui.graphics.Color

interface Palette {
    val backgroundPrimary: Color
    val backgroundSecondary: Color
    val contentPrimary: Color
    val contentSecondary: Color
    val contentTertiary: Color
    val textPrimary: Color
    val textSecondary: Color
    val textTertiary: Color
    val iconPrimary: Color
    val buttonRippleOnContent: Color
    val divider: Color
    val error: Color
    val success: Color
    val isDark:Boolean
}