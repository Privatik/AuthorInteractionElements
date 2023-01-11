package com.io.survey

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.GraphicsLayerScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Composable
fun rememberLeftSwipableState(initializeIndex: Int = 0, countItems: Int): LeftSwipableState{
    return rememberSaveable(saver = LeftSwipableState.Saver) {
        LeftSwipableState(initializeIndex, countItems)
    }
}

class LeftSwipableState(
    initializeIndex: Int = 0,
    val countItems: Int,
) {
    private var width: Float = 0f
    private val decayAnimationSpec = SpringSpec<Offset>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow,
    )

    private val animatable = Animatable(
        initialValue = Offset.Zero,
        typeConverter = Offset.VectorConverter
    )

    var lastInteractionIndex: Int by mutableStateOf(initializeIndex)
        private set
    val lastInteractionOffset: Offset
        get() = animatable.value

    var direction: Direction by mutableStateOf(Direction.SCROLL_TO_NEXT)
        private set

    suspend fun animateScrollTo(index: Int){
        if (index < 0 || index > countItems - 1){
            throw IndexOutOfBoundsException()
        }

        animatable.stop()
        determineDirectionForAutoScroll(index)

        settingBeforeScroll()
        while (lastInteractionIndex != index){
            val dragOffset = if (isScrollToNext()) Offset(-width, 0f) else Offset.Zero
            animateTo(dragOffset)
            settingBeforeScroll()
        }
        animateTo(Offset.Zero)
    }

    internal fun updateWidth(width: Float){
        this.width = width
    }

    internal fun availableRange(): IntRange{
        val leftBorder = (lastInteractionIndex - 1).coerceAtLeast(0)
        val rightBorder = (lastInteractionIndex + 1).coerceAtMost(countItems - 1)
        return leftBorder..rightBorder
    }

    internal fun determineDirection(offset: Offset){
        direction = if (offset.x >= 0f) Direction.SCROLL_TO_BACK else Direction.SCROLL_TO_NEXT
    }

    internal fun applyLayer(index: Int, scope: GraphicsLayerScope){
        scope.apply {
            if (isScrollToNext()){
                translationX = when {
                    index < lastInteractionIndex -> -width
                    index == lastInteractionIndex -> lastInteractionOffset.x
                    else -> 0f
                }
            } else {
                translationX = when {
                    index < lastInteractionIndex -> -width
                    index == lastInteractionIndex -> lastInteractionOffset.x
                    else -> 0f
                }
            }
        }

    }

    internal suspend fun stop(){
        coroutineScope {
            launch {
                settingBeforeScroll()
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

    internal suspend fun animateTo(offset: Offset, velocity: Offset = animatable.velocity){
        animatable.animateTo(
            initialVelocity = Offset(velocity.x, 0f),
            animationSpec = decayAnimationSpec,
            targetValue = Offset(calculateFinalOffsetX(offset), 0f)
        )
    }

    private fun determineDirectionForAutoScroll(newIndex: Int){
        direction = if (lastInteractionIndex > newIndex) Direction.SCROLL_TO_BACK else Direction.SCROLL_TO_NEXT
    }

    private suspend fun settingBeforeScroll(){
        val target = animatable.targetValue
        when {
            target.x == -width
                    && isScrollToNext()
                    && lastInteractionIndex != countItems - 1 -> {
                settingBeforeScrollToNext()
            }
            target.x == 0f
                    && !isScrollToNext()
                    && lastInteractionIndex != 0 -> {
                settingBeforeScrollToBack()
            }
            else -> {}
        }
    }

    private suspend fun settingBeforeScrollToNext(){
        coroutineScope {
            launch {
                lastInteractionIndex = (lastInteractionIndex + 1)
                    .coerceAtMost(countItems - 1)
            }
            launch {
                animatable.snapTo(Offset.Zero)
            }
        }
    }

    private suspend fun settingBeforeScrollToBack(){
        lastInteractionIndex = (lastInteractionIndex - 1)
            .coerceAtLeast(0)
        animatable.snapTo(Offset(-width, 0f))
    }

    private fun calculateFinalOffsetX(offset: Offset): Float{
        return  if (isScrollToNext()){
            if (offset.x < -width / 2 && lastInteractionIndex != countItems - 1) -width else 0f
        } else {
            if (offset.x > -width / 2) 0f else -width
        }
    }

    private fun isScrollToNext(): Boolean = direction == Direction.SCROLL_TO_NEXT

    enum class Direction{
        SCROLL_TO_NEXT,
        SCROLL_TO_BACK
    }

    companion object{
        val Saver = Saver<LeftSwipableState, List<Int>>(
            save = { state -> listOf(state.lastInteractionIndex, state.countItems) },
            restore = { list -> LeftSwipableState(list[0],list[1]) }
        )
    }
}