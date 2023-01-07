package com.io.item


import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.sp

@Composable
fun InteractionText(
    modifier: Modifier,
    text: String,
    pattern: String,
    textPlaceable: @Composable (
        paint: Paint,
        maxWidth: Int,
        text: String,
    ) -> Unit,
    interactionPlaceable: @Composable (
        maxWidth: Int,
        foundPattern: String,
    ) -> Unit,
){
    val helper = remember { InteractionHelper(pattern, text.split(" ")) }

    BoxWithConstraints(
        modifier = modifier
    ) {
        Layout(
            modifier = Modifier.fillMaxSize(),
            content = {
                helper.ShowItemsInteractionElements(
                    textPlaceable = textPlaceable,
                    interactionPlaceable = interactionPlaceable,
                )
            }
        ) { measurables, outerContraints ->

            layout(outerContraints.maxWidth, outerContraints.maxHeight){

            }
        }
    }

}

private class InteractionHelper(
    pattern: String,
    splitText: List<String>,
){
    private val regex = Regex(pattern)
    private val elements = mutableListOf<Element>()

    init {
        val builder = StringBuilder()

        splitText.forEach { item ->
            if (regex.containsMatchIn(item)){
                elements.add(Element.Text(builder.toString()))

                val notRightPatten = item.takeLastWhile { pattern.last() != it }
                val rightPattern = item.dropLastWhile { pattern.last() != it }

                elements.add(Element.Interaction(rightPattern))
                builder.clear()

                if (notRightPatten.isNotBlank()){
                    builder.append("$notRightPatten ")
                }
            } else {
                builder.append("$item ")
            }
        }

        if (builder.isNotBlank()){
            elements.add(Element.Text(builder.toString()))
        }
    }

    sealed interface Element{
        data class Text(val text: String): Element
        data class Interaction(val foundPattern: String): Element
    }

    @Composable
    fun ShowItemsInteractionElements(
        textPlaceable: @Composable (
            currentWidth: Int,
            maxWidth: Int,
            text: String,
        ) -> Unit,
        interactionPlaceable: @Composable (
            currentWidth: Int,
            maxWidth: Int,
            foundPattern: String,
        ) -> Unit,
    ){
        elements.forEach {
            when(it){
                is Element.Text -> {
                    textPlaceable(0,0, it.text)
                }
                is Element.Interaction -> {
                    interactionPlaceable(0, 0, it.foundPattern)
                }
            }
        }
    }
}