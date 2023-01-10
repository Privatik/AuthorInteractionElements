package com.io.item

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch
import kotlin.math.max

fun Modifier.rippleBackground(
    initializeOffset: (IntSize) -> Offset = { size ->
        Offset((size.width / 2).toFloat(), (size.height / 2).toFloat())
    },
    isDrawRippleBackground: Boolean = false,
    isHandleClickable: Boolean = false,
    color: Color,
    onClick: () -> Unit = {},
): Modifier = composed {
    var size by remember { mutableStateOf<IntSize>(IntSize.Zero) }
    var lastClickPosition by remember(size) { mutableStateOf(initializeOffset(size)) }
    val maxRadius = remember(size) { max(size.width.toFloat(), size.height.toFloat()) }
    val radius = remember { Animatable(0f) }

    LaunchedEffect(isDrawRippleBackground){
        if (isDrawRippleBackground){
            launch {
                radius.animateTo(
                    targetValue = maxRadius,
                    animationSpec = tween(durationMillis = 400),
                )
            }
        } else {
            launch {
                radius.snapTo(0f)
            }
        }

    }

    Modifier
        .pointerInput(isHandleClickable) {
            if (isHandleClickable) {
                detectTapGestures(
                    onTap = {
                        lastClickPosition = it
                        onClick()
                    }
                )
            }
        }
        .clip(MaterialTheme.shapes.small)
        .drawBehind {
            if (isDrawRippleBackground) {
                drawCircle(
                    color = color,
                    radius = radius.value,
                    center = lastClickPosition,
                )
            }
        }
        .onSizeChanged {
            if (it != size){
                size = it
            }
        }
}

fun Modifier.negativeAnswer(
    doAnimate: Boolean,
    color: Color,
    shape: Shape = RectangleShape,
    maxOffset: Float = 10f,
    countTwitching: Int = 3,
    timeTwitching: Int = 200,
    onFinishedAnimate: () -> Unit,
) = composed {
    val animateTransformX = remember { Animatable(0f) }

    LaunchedEffect(doAnimate){
        if (doAnimate){
            launch {
                repeat(countTwitching){
                    animateTransformX.animateTo(maxOffset, tween(timeTwitching))
                    animateTransformX.animateTo(-maxOffset, tween(timeTwitching))
                }
                onFinishedAnimate()
            }
        } else {
            if (animateTransformX.targetValue != 0f){
                animateTransformX.snapTo(0f)
            }
        }
    }

    val supportModifier = if (doAnimate){
        Modifier
            .drawBehind {
                val radius = max(size.width, size.height)
                drawCircle(color, radius, Offset(size.width / 2, size.height / 2))
            }
    } else {
        Modifier
    }

    Modifier
        .graphicsLayer {
            clip = true
            this.shape = shape
            translationX = animateTransformX.value
        }
        .then(supportModifier)
}