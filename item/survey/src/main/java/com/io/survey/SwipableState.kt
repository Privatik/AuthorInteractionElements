package com.io.survey

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@Composable
fun rememberSwipableState(initializeIndex: Int = 0, countItems: Int): SwipableState{
    return remember {
        SwipableState(initializeIndex, countItems)
    }
}

class SwipableState(
    initializeIndex: Int = 0,
    val countItems: Int,
) {
    private var width: Int = 0
    var currentIndex: Int by mutableStateOf<Int>(initializeIndex)
        private set

    internal fun updateWidth(width: Int){
        this.width = width
    }

    private val decayAnimationSpec = SpringSpec<Offset>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow,
    )

    private val animatable = Animatable(
        initialValue = Offset.Zero,
        typeConverter = Offset.VectorConverter
    )

    val currentOffset: Offset
        get() = animatable.value

    internal fun <T> visibleItems(items: List<T>): IntRange{
        val startIndex = (currentIndex - 1).coerceAtLeast(0)
        val endIndex = (currentIndex + 1).coerceAtMost(items.lastIndex)
        return startIndex..endIndex
    }

    internal suspend fun stop(){
        println("stop animate")
        animatable.stop()
    }

    internal suspend fun snapTo(offset: Offset){
        animatable.snapTo(
            targetValue = Offset(offset.x, 0f)
        )
    }

    internal suspend fun animateTo(offset: Offset,  velocity: Offset){
        val correctSwipeX = if (offset.x < -width / 2){
            -width.toFloat()
        }  else {
            0f
        }

        animatable.animateTo(
            initialVelocity = Offset(velocity.x, 0f),
            animationSpec = decayAnimationSpec,
            targetValue = Offset(correctSwipeX, 0f)
        )
        println("finish animate")

        if (correctSwipeX < 0f){
            println("up currentIndex")
            currentIndex += 1
            animatable.snapTo(Offset.Zero)
        }
    }
}