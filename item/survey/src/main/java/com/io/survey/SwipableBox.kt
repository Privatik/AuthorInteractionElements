package com.io.survey

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import com.io.item.InteractionText
import java.lang.Integer.max

@Composable
fun SwipableBox(
    modifier: Modifier = Modifier,
    state: SwipableState,
    item: @Composable (index: Int) -> Unit,
){

    Layout(
        modifier = modifier
            .swipable(state)
            .background(Color.Red),
        content = {
            repeat(state.countItems) { index -> item(index) }
        }
    ) { measurables, outerContraints ->

        var maxWidth = outerContraints.minWidth
        var maxHeight = outerContraints.minHeight
        val placeables = measurables.map { measurable ->
            measurable.measure(outerContraints).apply {
                maxWidth = max(maxWidth, width)
                maxHeight = max(maxHeight, height)
            }
        }

        state.updateWidth(maxWidth)

        layout(maxWidth, maxHeight){
            placeables.forEachIndexed { index, placeable ->

                placeable.placeWithLayer(
                    position = IntOffset.Zero,
                    zIndex = (placeables.size - index).toFloat(),
                    layerBlock = {
                        translationX = when {
                            index < state.currentIndex -> -maxWidth.toFloat()
                            index == state.currentIndex -> state.currentOffset.x
                            else -> 0f
                        }
                    }
                )
            }
//            state.visibleItems(placeables).forEach { index ->
//                val placeable = placeables[index]
//
//                val correctOffset =
//                    IntOffset.Zero + IntOffset(state.currentOffset.x.toInt(), state.currentOffset.y.toInt())
//
//                println("layout new $correctOffset")
//                placeable.placeWithLayer(
//                    position = correctOffset,
//                    zIndex = (placeables.lastIndex - index).toFloat(),
//                )
//            }
        }
    }
}