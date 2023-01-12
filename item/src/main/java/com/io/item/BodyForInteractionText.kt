package com.io.item

data class BodyForInteractionText(
    val text: String,
    val pattern: String,
    val indexAnsweredBlocks: Set<Int>,
)
