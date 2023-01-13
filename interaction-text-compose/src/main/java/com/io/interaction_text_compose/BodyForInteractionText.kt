package com.io.interaction_text_compose

data class BodyForInteractionText(
    val text: String,
    val pattern: String,
    val indexAnsweredBlocks: Set<Int>,
)
