package com.io.stars

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

fun starsOffsets(
    countStars: Int,
    sizeOneStar: Size,
    paddingBetweenStar: Float
): List<Animatable<Offset, AnimationVector2D>> =  List(countStars) { index ->
    Animatable(Offset((index * (sizeOneStar.width + paddingBetweenStar)), 0f), Offset.VectorConverter)
}