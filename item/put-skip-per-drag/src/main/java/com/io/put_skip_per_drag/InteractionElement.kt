package com.io.put_skip_per_drag

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

data class InteractionElement(
    val id: Int,
    val value: Any,
)

data class InteractionRect(
    val offset: Offset,
    val size: IntSize,
)