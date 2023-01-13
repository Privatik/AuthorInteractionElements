package com.io.put_skip_per_drag

import androidx.compose.ui.geometry.Offset

class ChangePositionObserver {
    private val interactionBlocks = mutableListOf<PositionConfig>()

    fun findMatching(offset: Offset, element: InteractionElement){
        synchronized(this){
            interactionBlocks.forEach { moveElement ->
                if (moveElement.isMatching(offset)){
                    moveElement.matchBlock(element)
                }
            }
        }
    }

    internal fun subscribe(config: PositionConfig) {
        synchronized(this) {
            interactionBlocks.add(config)
        }
    }

    internal fun unsubscribe(config: PositionConfig){
        synchronized(this) {
            interactionBlocks.remove(config)
        }
    }
}