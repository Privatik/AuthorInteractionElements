@file:OptIn(ExperimentalTextApi::class)

package com.io.stars

import android.graphics.Region
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun Path.star(
    topLeft: Offset,
    size: Size,
): Path {
    val borderRegion = Region(
        Rect(
            left = topLeft.x,
            top = topLeft.y,
            right = topLeft.x + size.width,
            bottom = topLeft.y + size.height,
        ).toAndroidRect()
    )

    val region = Region()

    reset()

    // top left
    moveTo(topLeft.x, topLeft.y + (size.height * 0.35f))
    // top right
    lineTo(topLeft.x + size.width, topLeft.y + (size.height * 0.35f))
    // bottom left
    lineTo(topLeft.x + (size.width * 0.17f), topLeft.y + size.height)
    // top tip
    lineTo(topLeft.x + (size.width * 0.5f), topLeft.y)
    // bottom right
    lineTo(topLeft.x + (size.width * 0.83f), topLeft.y + size.height)

    close()

    region.setPath(this.asAndroidPath(), borderRegion)
    return region.boundaryPath.asComposePath()
}

fun List<Animatable<Offset, AnimationVector2D>>.indexClickedOnStar(
    clickOffset: Offset,
    size: Size,
): Int{
    var index = -1
    var counter = 0
    while (index != 1 && counter != this.size){
        val currentOffset = get(counter).value
        if (clickOffset.x in currentOffset.x..(currentOffset.x + size.width)
            && clickOffset.y in currentOffset.y..(currentOffset.y + size.height)){
            index = counter
        }
        counter++
    }
    return index
}

@Composable
fun Stars(
    modifier: Modifier = Modifier,
    countStars: Int = 5,
    sizeOneStar: Dp = 50.dp,
    paddingBetweenStar: Dp = 10.dp,
    star: Star,
    doEvaluate: (selectedStar: Int) -> Unit
){
    val density = LocalDensity.current
    val palette = palette
    val dimens = dimens

    val path = remember { Path() }

    val widthBlock = remember(density) {
        (sizeOneStar.times(countStars)) + paddingBetweenStar.times(countStars - 1)
    }
    val widthBlockFloat = remember(density) {
        with(density) { widthBlock.roundToPx() }.toFloat()
    }

    val sizeOneStarPx = remember(density) {
        with(density){ sizeOneStar.roundToPx() }.toFloat().let { Size(it,it) }
    }

    val paddingBetweenStarPx = remember(density) {
        with(density){ paddingBetweenStar.roundToPx() }.toFloat()
    }

    val offsetStars = remember {
        starsOffsets(
            countStars = countStars,
            sizeOneStar = sizeOneStarPx,
            paddingBetweenStar = paddingBetweenStarPx,
        )
    }

    val selectedStarts = remember { mutableStateOf(0) }

    val doAnimate = remember { mutableStateOf(false) }
    val isFinishOrdered = remember { mutableStateOf(false) }
    val isShowSelectedStars = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val widthSelect = remember { Animatable(0f) }

    val drawText = selectedStartsTextScope(
        isDrawTextState = isShowSelectedStars,
        selectedStartsState = selectedStarts,
        offsetAnimableState = offsetStars[0],
        starSize = sizeOneStarPx
    )

    val enabled = remember {
        derivedStateOf { widthSelect.targetValue != 0f }
    }
    
    LaunchedEffect(doAnimate.value){
        if (doAnimate.value){
            coroutineScope {
                offsetStars.forEachIndexed { index, animatable ->
                    if (index != 0){
                        launch {
                            delay(200 * index.toLong())
                            val targetValue = animatable.targetValue
                            animatable.animateTo(
                                Offset(targetValue.x, targetValue.y - (sizeOneStar.height * 2))
                            )
                            animatable.snapTo(Offset(0f, targetValue.y - (sizeOneStar.height * 2)))
                            animatable.animateTo(Offset.Zero)
                        }
                    }
                }
            }
            isFinishOrdered.value = true
            offsetStars[0].animateTo(Offset.Zero.copy(x = widthBlockFloat / 2 - sizeOneStar.width / 2))
            isShowSelectedStars.value = true
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Canvas(
            modifier = Modifier
                .width(widthBlock)
                .height(sizeOneStar)
                .pointerInput(doAnimate.value) {
                    if (!doAnimate.value) {
                        detectTapGestures(
                            onTap = { clickedOffset ->
                                val clickedIndex =
                                    offsetStars.indexClickedOnStar(clickedOffset, sizeOneStar)
                                if (clickedIndex != -1) {
                                    scope.launch {
                                        selectedStarts.value = clickedIndex
                                        widthSelect.stop()
                                        widthSelect.animateTo(
                                            targetValue = offsetStars[clickedIndex].value.x + sizeOneStar.width,
                                            tween(400)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
        ){
            val starsPath = Path()


            if (isFinishOrdered.value){
                drawPath(
                    path.star(offsetStars[0].value, sizeOneStar).also { starPath -> starsPath.addPath(starPath) },
                    color = palette.contentTertiary,
                    style = Stroke(
                        width = 3f
                    )
                )
            } else {
                offsetStars.forEachIndexed { index, animable ->
                    drawPath(
                        path.star(animable.value, sizeOneStar).also { starPath -> starsPath.addPath(starPath) },
                        color = palette.contentTertiary,
                        style = Stroke(
                            width = 3f
                        )
                    )
                }
            }


            clipPath(starsPath){
                val width = if (isFinishOrdered.value) size.width else widthSelect.value

                drawRect(
                    color = palette.contentTertiary,
                    topLeft = Offset(0f, -(sizeOneStar.height * 2)),
                    size = Size(width = width, height = sizeOneStar.height * 3)
                )
            }

            drawText()
        }

        Spacer(modifier = Modifier.height(ProjectTheme.dimens.smallSpace))

        EvaluateButton(
            enabled = enabled.value
        ) {
            scope.launch {
                doAnimate.value = true
            }
        }
    }
}

@Composable
private fun EvaluateButton(
    enabled: Boolean,
    onClick: () -> Unit
){
    val backgroundButtonColor by animateColorAsState(
        targetValue = if (enabled) palette.backgroundSecondary else palette.backgroundPrimary
    )
    val textColor by animateColorAsState(
        targetValue = if (enabled) palette.textSecondary else palette.textTertiary
    )

    Button(
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundButtonColor,
            disabledBackgroundColor = backgroundButtonColor
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text = stringResource(R.string.evaluate),
            color = textColor
        )
    }
}