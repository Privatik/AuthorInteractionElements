package com.io.interaction_text_compose

import androidx.compose.runtime.Composable

interface InteractionTextManager<T: Any> {
    fun <S : Any> interactionElements(
        transform: (T) -> S
    ): List<S>
}

internal class InteractionTextManagerImpl(
    interactionBody: BodyForInteractionText,
): InteractionTextManager<String> {
    private val regex = Regex(interactionBody.pattern)
    private val textElements = mutableListOf<TextElement>()

    init {
        val splitText = interactionBody.text.split(" ")
        var patternIndex = 0

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

                textElements.add(
                    TextElement.Interaction(
                        index = patternIndex++,
                        beforePatternText = beforePatternText,
                        afterPatternText = afterPatternText,
                        foundPattern = item,
                    )
                )

            } else {
                textElements.add(TextElement.Text("$item$additionalSpace"))
            }
        }

    }

    sealed interface TextElement{
        data class Text(val text: String): TextElement
        data class Interaction(
            val index: Int,
            val beforePatternText: String,
            val afterPatternText: String,
            val foundPattern: String
        ): TextElement
    }

    @Composable
    fun ShowItemsInteractionElements(
        textPlaceable: @Composable (
            text: String,
        ) -> Unit,
        interactionPlaceable: @Composable (
            index: Int,
            beforePatternText: String,
            afterPatternText: String,
            foundPattern: String,
        ) -> Unit,
    ){
        textElements.forEach {
            when(it){
                is TextElement.Text -> {
                    textPlaceable(it.text)
                }
                is TextElement.Interaction -> {
                    interactionPlaceable(
                        index = it.index,
                        beforePatternText = it.beforePatternText,
                        afterPatternText = it.afterPatternText,
                        foundPattern = it.foundPattern,
                    )
                }
            }
        }
    }

    override fun <S : Any> interactionElements(transform: (String) -> S): List<S> =
        textElements.filterIsInstance<TextElement.Interaction>().map { transform(it.foundPattern) }
}