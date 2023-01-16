package com.io.stars

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.palette
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun Modifier.starsMovement(
    isStartAnimation: State<Boolean>,
    isShowResult: State<Boolean>,
    animableOffsetsStars: List<Animatable<Offset, AnimationVector2D>>,
    sizeOneStarPx: Size,
    finishJumpAnimation: suspend () -> Unit,
    finishMoveToCenterAnimation: suspend () -> Unit,
) = composed{

    val centerWidthStarts = remember {
        (animableOffsetsStars.last().targetValue.x + sizeOneStarPx.width) / 2
    }

    LaunchedEffect(isStartAnimation.value, isShowResult.value){
        if (isStartAnimation.value){
            coroutineScope {
                animableOffsetsStars.forEachIndexed { index, animatable ->
                    if (index != 0){
                        launch {
                            delay(200 * index.toLong())
                            val targetValue = animatable.targetValue
                            animatable.animateTo(
                                Offset(targetValue.x, targetValue.y - (sizeOneStarPx.height * 2))
                            )
                            animatable.snapTo(Offset(0f, targetValue.y - (sizeOneStarPx.height * 2)))
                            animatable.animateTo(Offset.Zero)
                        }
                    }
                }
            }
            finishJumpAnimation()
            animableOffsetsStars[0].animateTo(Offset.Zero.copy(x = centerWidthStarts - sizeOneStarPx.width / 2))
            finishMoveToCenterAnimation()
        }
        else if (isShowResult.value){
            animableOffsetsStars[0].snapTo(Offset.Zero.copy(x = centerWidthStarts - sizeOneStarPx.width / 2))
        }
    }

    Modifier
}

internal fun Modifier.drawStarts(
    starsPath: Path,
    isDrawOnlyFirstStar: State<Boolean>,
    animableOffsetsStars: List<Animatable<Offset, AnimationVector2D>>,
    sizeOneStar: Size
) = composed {

    val palette = palette
    val starPath = remember { Path() }

    Modifier.drawBehind {
        starsPath.reset()
        if (isDrawOnlyFirstStar.value){
            drawStar(
                path = starPath,
                starsPath = starsPath,
                topLeft = animableOffsetsStars[0].value,
                sizeOneStar = sizeOneStar,
                palette = palette
            )
        } else {
            animableOffsetsStars.forEach { animable ->
                drawStar(
                    path = starPath,
                    starsPath = starsPath,
                    topLeft = animable.value,
                    sizeOneStar = sizeOneStar,
                    palette = palette
                )
            }
        }
    }
}

internal fun Modifier.drawStarsProgressLineByPath(
    starsPath: Path,
    canHandleClicks: State<Boolean>,
    selectedStars: State<Int>,
    animableOffsetsStars: List<Animatable<Offset, AnimationVector2D>>,
    sizeOneStarPx: Size,
    selectStars: (Int) -> Unit,
) = composed {

    val palette = palette
    val widthProgressLine = remember {
        Animatable(
            if (selectedStars.value == 0) 0f
            else animableOffsetsStars[selectedStars.value - 1].value.x + sizeOneStarPx.width
        )
    }

    Modifier
        .pointerInput(canHandleClicks.value) {
            if (canHandleClicks.value) {
                coroutineScope {
                    detectTapGestures(
                        onTap = { clickedOffset ->
                            val clickedIndex =
                                animableOffsetsStars.indexClickedOnStar(
                                    clickedOffset,
                                    sizeOneStarPx
                                )
                            if (clickedIndex != -1) {
                                println("click $clickedIndex")
                                launch {
                                    selectStars(clickedIndex + 1)
                                    widthProgressLine.stop()
                                    widthProgressLine.animateTo(
                                        targetValue = animableOffsetsStars[clickedIndex].value.x + sizeOneStarPx.width,
                                        tween(400)
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
        .drawBehind {
            clipPath(starsPath) {
                val width = if (!canHandleClicks.value) size.width else widthProgressLine.value

                drawRect(
                    color = palette.contentSecondary,
                    topLeft = Offset(0f, -(sizeOneStarPx.height * 2)),
                    size = Size(width = width, height = sizeOneStarPx.height * 3)
                )
            }
        }
}

@OptIn(ExperimentalTextApi::class)
internal fun Modifier.drawCountSelectedStarts(
    isDrawTextState: State<Boolean>,
    selectedStartsState: State<Int>,
    offsetAnimableState: Animatable<Offset, AnimationVector2D>,
    starOneSize: Size,
) = composed{

    val textMeasurer = rememberTextMeasurer()
    val dimens = ProjectTheme.dimens
    val palette = ProjectTheme.palette

    val textLayoutResult: TextLayoutResult = remember(selectedStartsState.value) {
        textMeasurer.measure(
            text = AnnotatedString("${selectedStartsState.value}"),
            style = TextStyle.Default.copy(fontSize = dimens.justVariationFontSize)
        )
    }

    val alphaText by animateFloatAsState(
        targetValue = if (isDrawTextState.value) 1f else 0f,
        animationSpec = tween(DefaultAlphaAnimationDuration)
    )

    Modifier.drawBehind {
        if (isDrawTextState.value){
            val currentTopLeft = offsetAnimableState.targetValue
            val topLeft = Offset(
                x = (currentTopLeft.x + starOneSize.width / 2) - textLayoutResult.size.width / 2,
                y = (currentTopLeft.y + starOneSize.height / 2) - textLayoutResult.size.height / 2
            )
            drawText(
                textLayoutResult = textLayoutResult,
                color = palette.textPrimary,
                alpha = alphaText,
                topLeft = topLeft
            )
        }
    }
}

private val DefaultAlphaAnimationDuration = 800