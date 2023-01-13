@file:OptIn(ExperimentalTextApi::class)

package com.io.stars

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.*
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens

@Composable
fun selectedStartsTextScope(
    isDrawTextState: State<Boolean>,
    selectedStartsState: State<Int>,
    offsetAnimableState: Animatable<Offset, AnimationVector2D>,
    starSize: Size,
): DrawScope.() -> Unit{
    val textMeasurer = rememberTextMeasurer()
    val dimens = dimens
    val palette = ProjectTheme.palette

    val textLayoutResult: TextLayoutResult = remember(selectedStartsState.value) {
        textMeasurer.measure(
            text = AnnotatedString("${selectedStartsState.value + 1}"),
            style = TextStyle.Default.copy(fontSize = dimens.justVariationFontSize)
        )
    }

    val alphaText by animateFloatAsState(
        targetValue = if (isDrawTextState.value) 1f else 0f,
        animationSpec = tween(800)
    )

    return {
        if (isDrawTextState.value){
            val currentTopLeft = offsetAnimableState.targetValue
            val topLeft = Offset(
                x = (currentTopLeft.x + starSize.width / 2) - textLayoutResult.size.width / 2,
                y = (currentTopLeft.y + starSize.height / 2) - textLayoutResult.size.height / 2
            )
            drawText(
                textLayoutResult = textLayoutResult,
                color = palette.textSecondary,
                alpha = alphaText,
                topLeft = topLeft
            )
        }
    }
}