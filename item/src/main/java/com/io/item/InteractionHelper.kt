package com.io.item

import androidx.compose.animation.ContentTransform

interface InteractionHelper<T: Any> {
    fun <S : Any> interactionElements(
        transform: (T) -> S
    ): List<S>
}