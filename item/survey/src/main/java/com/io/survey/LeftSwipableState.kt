package com.io.survey

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.GraphicsLayerScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberLeftSwipableState(initializeIndex: Int = 0, countItems: Int): LeftSwipableState{
    return remember {
        LeftSwipableState(initializeIndex, countItems)
    }
}

class LeftSwipableState(
    initializeIndex: Int = 0,
    val countItems: Int,
) {
    private var width: Float = 0f
    var lastInteractionIndex: Int by mutableStateOf(initializeIndex)
        private set
    val lastInteractionOffset: Offset
        get() = animatable.value

    var direction: Direction by mutableStateOf(Direction.OPEN_NEXT_CARD)
        private set

    private val decayAnimationSpec = SpringSpec<Offset>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow,
    )

    private val animatable = Animatable(
        initialValue = Offset.Zero,
        typeConverter = Offset.VectorConverter
    )

    suspend fun animateScrollTo(index: Int){
        if (index < 0 || index > countItems - 1){
            throw IndexOutOfBoundsException()
        }

        direction = if (lastInteractionIndex > index) Direction.BACK_LAST_CARD else Direction.OPEN_NEXT_CARD
        do {
            lastInteractionIndex += if (isOpenNextDirection()) { 1 } else { -1 }
            animateTo(Offset(if (isOpenNextDirection()) -1f else 1f, 0f), animatable.velocity)
        } while (lastInteractionIndex != index)
    }

    internal fun updateWidth(width: Float){
        this.width = width
    }

    internal fun determineDirection(offset: Offset){
        direction = if (offset.x >= 0f) Direction.BACK_LAST_CARD else Direction.OPEN_NEXT_CARD
    }

    internal fun applyLayer(index: Int, scope: GraphicsLayerScope){
        scope.apply {
            if (isOpenNextDirection()){
                translationX = when {
                    index < lastInteractionIndex -> -width
                    index == lastInteractionIndex -> lastInteractionOffset.x
                    else -> 0f
                }
            } else {
                translationX = when {
                    index < lastInteractionIndex-> -width
                    index == lastInteractionIndex -> lastInteractionOffset.x
                    else -> 0f
                }
            }
        }

    }

    internal suspend fun stop(){
        coroutineScope {
            when {
                animatable.targetValue.x == -width
                        && isOpenNextDirection()
                        && lastInteractionIndex != countItems - 1 -> {
                    launch {
                        lastInteractionIndex = (lastInteractionIndex + 1)
                            .coerceAtMost(countItems - 1)
                        animatable.snapTo(Offset.Zero)
                    }
                }
                animatable.targetValue.x == 0f
                        && !isOpenNextDirection()
                        && lastInteractionIndex != 0 -> {
                    launch {
                        lastInteractionIndex = (lastInteractionIndex - 1)
                            .coerceAtLeast(0)
                        animatable.snapTo(Offset(-width, 0f))
                    }
                }
                else -> {}
            }
        }

        animatable.stop()
    }

    internal suspend fun snapTo(offset: Offset){
        val leftBorder = if (lastInteractionIndex == countItems - 1) 0f else -width
        val rightBorder = 0f
        animatable.snapTo(
            targetValue = Offset(offset.x.coerceIn(leftBorder, rightBorder), 0f)
        )
    }

    internal suspend fun animateTo(offset: Offset,  velocity: Offset){
        val animateOffsetX = if (isOpenNextDirection()){
            if (offset.x < -width / 2 && lastInteractionIndex != countItems - 1) -width else 0f
        } else {
            if (offset.x > -width / 2) 0f else -width
        }

        animatable.animateTo(
            initialVelocity = Offset(velocity.x, 0f),
            animationSpec = decayAnimationSpec,
            targetValue = Offset(animateOffsetX, 0f)
        )
    }

    private fun isOpenNextDirection(): Boolean = direction == Direction.OPEN_NEXT_CARD

    enum class Direction{
        OPEN_NEXT_CARD,
        BACK_LAST_CARD
    }

    companion object{
        val Saver = Saver<LeftSwipableState, List<Int>>(
            save = { state -> listOf(state.lastInteractionIndex, state.countItems) },
            restore = { list -> LeftSwipableState(list[0],list[1]) }
        )
    }
}