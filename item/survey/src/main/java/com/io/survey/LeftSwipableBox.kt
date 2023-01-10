package com.io.survey

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import java.lang.Integer.max

@Composable
fun LeftSwipableBox(
    modifier: Modifier = Modifier,
    state: LeftSwipableState,
    userScrollEnabled: Boolean = true,
    item: @Composable (index: Int) -> Unit,
){

    Layout(
        modifier = modifier
            .then(if (userScrollEnabled) Modifier.swipable(state) else Modifier),
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

        state.updateWidth(maxWidth.toFloat())

        layout(maxWidth, maxHeight){
            placeables.forEachIndexed { index, placeable ->

                placeable.placeWithLayer(
                    position = IntOffset.Zero,
                    zIndex = (placeables.size - index).toFloat(),
                    layerBlock = { state.applyLayer(index, this) }
                )
            }
        }
    }
}