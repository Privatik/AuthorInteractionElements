package com.io.item


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
    text: String,
    pattern: String,
    getInteractionHelper: (InteractionHelper<String>) -> Unit = {},
    textPlaceable: @Composable (
        text: String,
    ) -> Unit,
    interactionPlaceable: @Composable (
        beforePatternText: String,
        afterPatternText: String,
        foundPattern: String,
    ) -> Unit,
){
    val helper = remember {
        InteractionHelperImpl(pattern, text.split(" "))
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

private class InteractionHelperImpl(
    pattern: String,
    splitText: List<String>,
): InteractionHelper<String> {
    private val regex = Regex(pattern)
    private val elements = mutableListOf<Element>()

    init {
        splitText.forEachIndexed { index, item ->
            val additionalSpace = if (index == splitText.lastIndex) "" else " "

            if (regex.containsMatchIn(item)){
                val result = regex.find(item)
                val beforePatternText = result?.let {
                    item.substring(0, it.range.first)
                } ?: ""
                val afterPatternText = result?.let {
                    if (it.range.last + 1 != item.length){
                        "${item.substring(it.range.last + 1, item.length)}$additionalSpace"
                    } else additionalSpace
                } ?: additionalSpace

                elements.add(Element.Interaction(
                    beforePatternText = beforePatternText,
                    afterPatternText = afterPatternText,
                    foundPattern = item,
                ))
            } else {
                elements.add(Element.Text("$item$additionalSpace"))
            }
        }

    }

    sealed interface Element{
        data class Text(val text: String): Element
        data class Interaction(
            val beforePatternText: String,
            val afterPatternText: String,
            val foundPattern: String
        ): Element
    }

    @Composable
    fun ShowItemsInteractionElements(
        textPlaceable: @Composable (
            text: String,
        ) -> Unit,
        interactionPlaceable: @Composable (
            beforePatternText: String,
            afterPatternText: String,
            foundPattern: String,
        ) -> Unit,
    ){
        elements.forEach {
            when(it){
                is Element.Text -> {
                    textPlaceable(it.text)
                }
                is Element.Interaction -> {
                    interactionPlaceable(
                        beforePatternText = it.beforePatternText,
                        afterPatternText = it.afterPatternText,
                        foundPattern = it.foundPattern,
                    )
                }
            }
        }
    }

    override fun <S : Any> interactionElements(transform: (String) -> S): List<S> =
        elements.filterIsInstance<Element.Interaction>().map { transform(it.foundPattern) }
}