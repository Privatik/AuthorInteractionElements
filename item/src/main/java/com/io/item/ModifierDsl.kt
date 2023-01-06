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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.launch
import kotlin.math.max

fun Modifier.changeBackgroundPerRipple(
    isDrawRippleBackground: Boolean,
    isHandleClickable: Boolean,
    background: Color,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickPosition by remember { mutableStateOf(Offset.Unspecified) }
    val radius = remember { Animatable(0f) }
    var maxRadius by remember { mutableStateOf(-1f) }

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

    Modifier.pointerInput(isHandleClickable) {
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
            if (lastClickPosition.isUnspecified){
                lastClickPosition = Offset(size.width / 2, size.height / 2)
            }

            if (isHandleClickable && isDrawRippleBackground) {
                drawCircle(
                    color = background,
                    radius = radius.value,
                    center = lastClickPosition,
                )
            }
        }
        .onSizeChanged {
            if (maxRadius == -1f) {
                maxRadius = max(it.width.toFloat(), it.height.toFloat())
            }
        }
}