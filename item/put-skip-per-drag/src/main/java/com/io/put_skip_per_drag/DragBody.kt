package com.io.put_skip_per_drag

import androidx.compose.ui.geometry.Offset

class PositionConfig(
    interactionRect: InteractionRect,
    val matchBlock: (InteractionElement) -> Unit,
) {

    private val rangeX = interactionRect.offset.x..(interactionRect.offset.x + interactionRect.size.width)
    private val rangeY = interactionRect.offset.y..(interactionRect.offset.y + interactionRect.size.height)

    fun isMatching(offset: Offset): Boolean =
        offset.x in rangeX
                && offset.y in rangeY
}
