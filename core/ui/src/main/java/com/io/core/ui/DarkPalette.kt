package com.io.core.ui

import androidx.compose.ui.graphics.Color
import com.io.core.ui.Palette

internal object DarkPalette: Palette {
    override val backgroundPrimary: Color = Color.Black
    override val backgroundSecondary: Color = Color.White
    override val backgroundTertiary: Color = Color( 0xFFe5e4e2)
    override val contentPrimary: Color = Color( 0xFFe5e4e2)
    override val contentSecondary: Color = Color.Black
    override val contentTertiary: Color = Color.White
    override val textPrimary: Color = Color.White
    override val textSecondary: Color = Color.Black
    override val textTertiary: Color = Color( 0xFFe5e4e2)
    override val iconPrimary: Color = Color.White
    override val buttonRippleOnContent: Color = Color.White
    override val divider: Color = Color.White.copy(alpha = 0.8f)
    override val error: Color = Color(0xFFFF5E5E)
    override val success: Color = Color(0xFF3caa3c)
    override val isDark: Boolean = true
}