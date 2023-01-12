package com.io.authorinteractionelements.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.io.authorinteractionelements.R
import com.io.core.ui.LocalPaletteColors
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens
import com.io.item.BodyForInteractionText
import com.io.item.InteractionText
import com.io.item.negativeAnswer
import com.io.put_skip_per_drag.ChangePositionObserver
import com.io.put_skip_per_drag.DragElementsRow
import com.io.put_skip_per_drag.InteractionElement
import com.io.put_skip_per_drag.containerForDraggableItem
import java.util.*

private const val Mock = "1 пишется как ###|dragBlock|Один|###, а 2 пишется как ###|dragBlock|Два|### а 3 пишется как ###|dragBlock|Три|###."

@Composable
fun PutSkipItemPerDragExampleScreen(
    modifier: Modifier = Modifier,
    interactionItems: List<BodyForInteractionText>,
    addIndexAsAnswered: (Int) -> Unit
){
    val palette = LocalPaletteColors.current
    val dimens = dimens
    val density = LocalDensity.current
    val border = remember { BorderStroke(2.dp, palette.backgroundPrimary) }

    Card(
        modifier = Modifier
            .padding(ProjectTheme.dimens.cardOutPadding),
        backgroundColor = palette.backgroundTertiary,
        shape = shapes.medium,
        border = border,
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(ProjectTheme.dimens.insidePadding)
        ) {
            Text(
                text = stringResource(R.string.put_skip_item),
                fontSize = ProjectTheme.dimens.questionFontSize,
                color = palette.textSecondary,
            )

            Spacer(modifier = Modifier.height(ProjectTheme.dimens.mediumSpace))

            Box(
                modifier = Modifier
                    .animateContentSize(),
            ) {
                val interactionElements = remember { mutableListOf<InteractionElement>().toMutableStateList() }
                val changePositionObserver = remember { ChangePositionObserver() }

                val offsetYForDragItems = remember {
                    mutableStateOf(dimens.mediumSpace)
                }

                InteractionText(
                    modifier = Modifier
                        .onSizeChanged {
                            val height = with(density) { it.height.toDp() }
                            if (dimens.mediumSpace + height != offsetYForDragItems.value){
                                offsetYForDragItems.value = dimens.mediumSpace + height
                            }
                        },
                    interactionBody = interactionItems[0],
                    getInteractionHelper = { helper ->
                        interactionElements.addAll(
                            helper
                                .interactionElements { foundPattern ->
                                    foundPattern.split("|")[2].lowercase(Locale.getDefault())
                                }
                                .filterIndexed { index, _ -> !interactionItems[0].indexAnsweredBlocks.contains(index) }
                                .mapIndexed { index, s -> InteractionElement(id = index,value = s) }
                        )
                    },
                    textPlaceable = { text ->
                        TextForPutSkipDrag(
                            text = text
                        )
                    },
                    interactionPlaceable = { index, beforeText, afterText, foundPattern ->
                        val rightAnswer = remember(foundPattern) {
                            foundPattern.split("|")[2].lowercase(Locale.getDefault())
                        }

                        val isAnswered by remember {
                            derivedStateOf { interactionItems[0].indexAnsweredBlocks.contains(index) }
                        }

                        val isError = remember {
                            mutableStateOf(false)
                        }

                        if (!isAnswered){
                            TextForPutSkipDrag(
                                text = beforeText
                            )
                            Box(
                                modifier = Modifier
                                    .height(ProjectTheme.dimens.defaultBlockHeight)
                                    .width(dimens.defaultBlockWidth)
                                    .background(
                                        color = if (isError.value) Color.Transparent else palette.contentTertiary,
                                        shape = shapes.medium
                                    )
                                    .negativeAnswer(
                                        doAnimate = isError.value,
                                        shape = shapes.medium,
                                        color = palette.error,
                                        onFinishedAnimate = {
                                            isError.value = false
                                        }
                                    )
                                    .containerForDraggableItem(
                                        observer = changePositionObserver,
                                        hoveringOnItem = {
                                            if (it.value == rightAnswer) {
                                                interactionElements.remove(it)
                                                addIndexAsAnswered(index)
                                            } else {
                                                isError.value = true
                                            }
                                        }
                                    )
                            )
                            TextForPutSkipDrag(
                                text = afterText
                            )
                        } else {
                            TextForPutSkipDrag(
                                text = "$beforeText$rightAnswer$afterText"
                            )
                        }
                    }
                )
                DragElementsRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    changePositionObserver = changePositionObserver,
                    interactionElements = interactionElements,
                    offsetYForDragItems = offsetYForDragItems
                )
            }
        }
    }
}

@Composable
private fun TextForPutSkipDrag(
    text: String
){
    Text(
        modifier = Modifier.height(dimens.defaultBlockHeight),
        text = text,
        fontSize = dimens.justVariationFontSize,
        color = ProjectTheme.palette.textSecondary
    )
}