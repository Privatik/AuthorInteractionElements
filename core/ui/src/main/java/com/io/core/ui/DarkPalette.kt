package com.io.core.ui

import androidx.compose.ui.graphics.Color
import com.io.core.ui.Palette

internal object DarkPalette: Palette {
    override val backgroundPrimary: Color = Color.Black
    override val backgroundSecondary: Color = Color.White
    override val contentPrimary: Color = Color.LightGray
    override val contentSecondary: Color = Color(color = 0xFFFF5317)
    override val contentTertiary: Color = Color.White
    override val textPrimary: Color = Color.White
    override val textSecondary: Color = Color.Black
    override val textTertiary: Color = Color.Gray
    override val iconPrimary: Color = Color.White
    override val buttonRippleOnContent: Color = Color.White
    override val divider: Color = Color.White.copy(alpha = 0.5f)
    override val error: Color = Color.Red.copy(alpha = 0.8f)
    override val success: Color = Color.Green.copy(alpha = 0.8f)
    override val isDark: Boolean = true
}