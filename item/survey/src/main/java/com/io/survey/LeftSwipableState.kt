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
internal fun rememberLeftSwipableState(initializeIndex: Int = 0, countItems: Int): LeftSwipableState{
    return rememberSaveable(saver = LeftSwipableState.Saver) {
        LeftSwipableState(initializeIndex, countItems)
    }
}

internal class LeftSwipableState(
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
            animateTo(
                offset = dragOffset,
                animationSpec = tween(DefaultDurationAnimationForAutoScroll),
                isAutoSwitchOnNextItem = false,
            )
        }
        animateTo(Offset.Zero)
    }

    fun updateWidth(width: Float){
        this.width = width
    }

    fun availableRange(): IntRange{
        val leftBorder = lastInteractionIndex
        val rightBorder = (lastInteractionIndex + 1).coerceAtMost(countItems - 1)
        return leftBorder..rightBorder
    }

    fun determineDirection(offset: Offset){
        direction = if (offset.x >= 0f) Direction.SCROLL_TO_BACK else Direction.SCROLL_TO_NEXT
    }

    fun applyLayer(index: Int, scope: GraphicsLayerScope){
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

    suspend fun stop(){
        coroutineScope {
            launch { animatable.stop() }
            launch { settingBeforeScroll() }
        }
    }

    suspend fun snapTo(offset: Offset){
        val leftBorder = if (lastInteractionIndex == countItems - 1) 0f else -width
        val rightBorder = 0f
        animatable.snapTo(
            targetValue = Offset(offset.x.coerceIn(leftBorder, rightBorder), 0f)
        )
    }

    suspend fun animateTo(
        offset: Offset,
        velocity: Offset = animatable.velocity,
        animationSpec: AnimationSpec<Offset> = decayAnimationSpec,
        isAutoSwitchOnNextItem: Boolean = true
    ){
        animatable.animateTo(
            initialVelocity = Offset(velocity.x, 0f),
            animationSpec = animationSpec,
            targetValue = Offset(calculateFinalOffsetX(offset), 0f)
        )
        if (isAutoSwitchOnNextItem) { determineDirectionToNext() }
        settingBeforeScroll()
    }

    private fun determineDirectionForAutoScroll(newIndex: Int){
        direction = if (lastInteractionIndex > newIndex) Direction.SCROLL_TO_BACK else Direction.SCROLL_TO_NEXT
    }

    private fun determineDirectionToNext(){
        direction = Direction.SCROLL_TO_NEXT
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
        lastInteractionIndex = (lastInteractionIndex + 1)
            .coerceAtMost(countItems - 1)
        animatable.snapTo(Offset.Zero)
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
        private const val DefaultDurationAnimationForAutoScroll = 600

        val Saver = Saver<LeftSwipableState, List<Int>>(
            save = { state -> listOf(state.lastInteractionIndex, state.countItems) },
            restore = { list -> LeftSwipableState(list[0],list[1]) }
        )
    }
}