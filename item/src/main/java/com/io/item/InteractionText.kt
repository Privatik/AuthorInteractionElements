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
    getHelper: (InteractionHelper<String>) -> Unit = {},
    textPlaceable: @Composable (
        text: String,
    ) -> Unit,
    interactionPlaceable: @Composable (
        foundPattern: String,
    ) -> Unit,
){
    val helper = remember {
        InteractionHelperImpl(pattern, text.split(" "))
            .apply(getHelper)
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

        layout(outerContraints.maxWidth, outerContraints.maxHeight){

            var yOffset = 0
            placeablesInRow.forEach { placeables ->
                var xOffset = 0

                var maxHeight = 0
                placeables.forEach { placeable ->
                    placeable.place(
                        x = xOffset,
                        y = yOffset
                    )

                    xOffset += placeable.width
                    maxHeight = max(maxHeight, placeable.height)
                }
                yOffset += maxHeight
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
        splitText.forEach { item ->
            if (regex.containsMatchIn(item)){
//                val punctuation = item.replace(regex){
//                    elements.add(Element.Interaction(it.value)); ""
//                }

                elements.add(Element.Interaction(item))

//                if (punctuation.isNotBlank()){
//                    elements.add(Element.Text("$punctuation "))
//                }
            } else {
                elements.add(Element.Text("$item "))
            }
        }

    }

    sealed interface Element{
        data class Text(val text: String): Element
        data class Interaction(val foundPattern: String): Element
    }

    @Composable
    fun ShowItemsInteractionElements(
        textPlaceable: @Composable (
            text: String,
        ) -> Unit,
        interactionPlaceable: @Composable (
            foundPattern: String,
        ) -> Unit,
    ){
        elements.forEach {
            when(it){
                is Element.Text -> {
                    textPlaceable(it.text)
                }
                is Element.Interaction -> {
                    interactionPlaceable(it.foundPattern)
                }
            }
        }
    }

    override fun <S : Any> interactionElements(transform: (String) -> S): List<S> =
        elements.filterIsInstance<Element.Interaction>().map { transform(it.foundPattern) }
}