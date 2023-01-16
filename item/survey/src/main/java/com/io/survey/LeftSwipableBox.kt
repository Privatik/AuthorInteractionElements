package com.io.survey

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import java.lang.Integer.max

@Composable
internal fun LeftSwipableBox(
    modifier: Modifier = Modifier,
    state: LeftSwipableState,
    userScrollEnabled: Boolean = true,
    item: @Composable (index: Int) -> Unit,
){

    Layout(
        modifier = modifier
            .then(if (userScrollEnabled) Modifier.swipable(state) else Modifier),
        content = {
            repeat(state.countItems){ index ->
                key(index) {
                    if (index in state.availableRange()){
                        item(index)
                    } else {
                        Box {}
                    }
                }
            }
        }
    ) { measurables, outerContraints ->

        var maxWidth = outerContraints.minWidth
        var maxHeight = outerContraints.minHeight
        val placeables = measurables.map { measurable ->
            measurable.measure(outerContraints).also { placeable ->
                maxWidth = max(maxWidth, placeable.width)
                maxHeight = max(maxHeight, placeable.height)
            }
        }

        state.updateWidth(maxWidth.toFloat())

        layout(maxWidth, maxHeight){
            state.availableRange().forEach { index ->
                val placeable = placeables[index]

                placeable.placeWithLayer(
                    position = IntOffset.Zero,
                    zIndex = (placeables.size - index).toFloat(),
                    layerBlock = { state.applyLayer(index, this) }
                )
            }
        }
    }
}