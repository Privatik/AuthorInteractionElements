package com.io.match_between_two_column

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import kotlinx.coroutines.launch

fun Modifier.animatableGone(
    state: LazyListState,
    index: Int,
    item: PairMatchItems,
    offsetOnOtherItems:(Int, Float) -> Unit,
    isItemGone: (PairMatchItems) -> Unit,
) = composed{
    val scope = rememberCoroutineScope()
    val animateYOffset = remember(state) { Animatable(0f) }
    val layoutInfoState = remember { derivedStateOf { state.layoutInfo } }

    if (item.isFound){
        val layoutInfo = remember(layoutInfoState.value) { layoutInfoState.value }
        layoutInfo.visibleItemsInfo
            .firstOrNull {
                it.index == index
            }
            ?.let { currentItemInfo ->
                scope.launch {
                    animateYOffset.animateTo(
                        targetValue = -layoutInfo.viewportEndOffset.toFloat(),
                        animationSpec = tween(3000)
                    ) {
                        val startItemOffset = currentItemInfo.offset + value
                        val endItemOffset = currentItemInfo.offset + currentItemInfo.size + value

                        layoutInfo.visibleItemsInfo.forEach { visibleItem ->
                            if (visibleItem.index == currentItemInfo.index) return@forEach

                            val visibleStartOffset = visibleItem.offset
                            val visibleEndOffset = visibleItem.offset + visibleItem.size
                            if (
                                startItemOffset.toInt() in visibleStartOffset..visibleEndOffset
                            ) {
                                val dif = visibleEndOffset - startItemOffset
                                offsetOnOtherItems(visibleItem.index, dif)
                            }
                        }

                        if (layoutInfo.viewportStartOffset > endItemOffset){
                            scope.launch { animateYOffset.stop() }
                            isItemGone(item)
                        }
                    }
                }
            }
    }


    Modifier
        .graphicsLayer {
            translationY = animateYOffset.value
        }
}