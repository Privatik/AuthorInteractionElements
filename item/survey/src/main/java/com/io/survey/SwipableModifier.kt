package com.io.survey

import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal fun Modifier.swipable(
    state: LeftSwipableState,
) = pointerInput(Unit){
    val decay = splineBasedDecay<Offset>(this)
    val tracker = VelocityTracker()
    var isFirstTab = false

    coroutineScope {
        fun finishDrag() {
            val (velX, velY) = tracker.calculateVelocity()
            val velocity = Offset(velX, velY)
            val targetOffset = decay.calculateTargetValue(
                typeConverter = Offset.VectorConverter,
                initialValue = state.lastInteractionOffset,
                initialVelocity = velocity
            )
            launch {
                state.animateTo(
                    offset = targetOffset,
                    velocity = velocity,
                )
            }
        }

        detectDragGestures(
            onDragStart = {
                launch { state.stop() }
                tracker.resetTracking()
                isFirstTab = true
            },
            onDrag = { change, dragAmount ->
                launch {
                    state.snapTo(state.lastInteractionOffset.plus(dragAmount))
                }
                if (!isFirstTab) {
                    tracker.addPointerInputChange(change)
                } else {
                    state.determineDirection(dragAmount)
                    isFirstTab = false
                }
            },
            onDragEnd = { finishDrag() },
            onDragCancel = { finishDrag() }
        )
    }
}