package com.io.interaction_text_compose


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import kotlin.math.max

private fun <T> MutableList<T>.getOrAutoAddAndGet(index: Int, defaultValue: (Int) -> T): T {
    return getOrElse(index) {
        val value = defaultValue(it)
        add(value)
        value
    }
}


@Composable
fun InteractionText(
    modifier: Modifier = Modifier,
    interactionBody: BodyForInteractionText,
    getInteractionHelper: (InteractionTextManager<String>) -> Unit = {},
    textPlaceable: @Composable (
        text: String,
    ) -> Unit,
    interactionPlaceable: @Composable (
        indexPatternItem: Int,
        beforePatternText: String,
        afterPatternText: String,
        foundPattern: String,
    ) -> Unit,
){
    val helper = remember {
        InteractionTextManagerImpl(interactionBody)
            .apply(getInteractionHelper)
    }

    Layout(
        modifier = modifier,
        content = {
            helper.ShowItemsInteractionElements(
                textPlaceable = textPlaceable,
                interactionPlaceable = interactionPlaceable,
            )
        }
    ) { measurables, outerContraints ->
        val placeablesInRow = mutableListOf<MutableList<Placeable>>()
        val rowsWidth = mutableListOf<Int>()
        var currentRow = 0

        val childConstraints = Constraints(
            maxWidth = outerContraints.maxWidth
        )

        fun canAddItemInRow(rowIndex: Int, placeable: Placeable): Boolean {
            return placeable.width + rowsWidth.getOrAutoAddAndGet(rowIndex) { 0 } < outerContraints.maxWidth
        }

        measurables.forEach { measurable ->
            val placeable = measurable.measure(childConstraints)

            if (!canAddItemInRow(currentRow, placeable)){
                currentRow++
            }

            placeablesInRow.getOrAutoAddAndGet(currentRow){
                mutableListOf()
            }.apply {
                add(placeable)
            }

            rowsWidth.getOrAutoAddAndGet(currentRow) { 0 }.also { width ->
                rowsWidth[currentRow] = width + placeable.width
            }
        }

        val widthLayoutSize =
            max(
                rowsWidth.maxOrNull() ?: 0,
                outerContraints.minWidth
            )


        val heightLayoutSize =
            max(
                placeablesInRow.fold(0){ currentHeight, nextRowPlaceables ->
                    currentHeight + (nextRowPlaceables.maxOfOrNull { it.height } ?: 0)
                },
                outerContraints.minHeight
            )

        rowsWidth.clear()

        layout(widthLayoutSize, heightLayoutSize){
            var yOffset = 0
            placeablesInRow.forEach { placeables ->
                var xOffset = 0

                var rowHeight = 0
                placeables.forEach { placeable ->
                    placeable.placeRelative(
                        x = xOffset,
                        y = yOffset
                    )

                    xOffset += placeable.width
                    rowHeight = max(rowHeight, placeable.height)
                }
                yOffset += rowHeight
            }
        }
    }
}