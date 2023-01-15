package com.io.authorinteractionelements.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.io.authorinteractionelements.R
import com.io.authorinteractionelements.components.DefaultCard
import com.io.authorinteractionelements.components.HeadForExample
import com.io.authorinteractionelements.components.SimpleTextForPutTask
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import com.io.interaction_text_compose.BodyForInteractionText
import com.io.interaction_text_compose.InteractionText
import com.io.item.negativeAnswer
import com.io.put_skip_per_drag.ChangePositionObserver
import com.io.put_skip_per_drag.DragElementsRow
import com.io.put_skip_per_drag.InteractionElement
import com.io.put_skip_per_drag.containerForDraggableItem
import java.util.*

@Composable
fun PutSkipItemPerDragExampleScreen(
    modifier: Modifier = Modifier,
    interactionTextItems: List<BodyForInteractionText>,
    addIndexAsAnswered: (index: Int, indexPattern: Int) -> Unit
){
    val density = LocalDensity.current
    val dimens = dimens

    HeadForExample(
        modifier = modifier,
        items = interactionTextItems
    ) { index, interactionText ->
        DefaultCard {
            Column(
                modifier = Modifier.padding(ProjectTheme.dimens.insidePadding)
            ) {
                Text(
                    text = stringResource(R.string.put_skip_item),
                    fontSize = dimens.questionFontSize,
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
                        interactionBody = interactionText,
                        getInteractionHelper = { helper ->
                            interactionElements.addAll(
                                helper
                                    .interactionElements { foundPattern ->
                                        foundPattern.split("|")[2].lowercase(Locale.getDefault())
                                    }
                                    .filterIndexed { index, _ ->
                                        !interactionTextItems[index].indexAnsweredBlocks.contains(index)
                                    }
                                    .mapIndexed { index, s -> InteractionElement(id = index,value = s) }
                            )
                        },
                        textPlaceable = { text ->
                            SimpleTextForPutTask(
                                text = text
                            )
                        },
                        interactionPlaceable = { indexPattern, beforeText, afterText, foundPattern ->
                            val rightAnswer = remember(foundPattern) {
                                foundPattern.split("|")[2].lowercase(Locale.getDefault())
                            }

                            val isAnswered by remember {
                                derivedStateOf { interactionTextItems[index].indexAnsweredBlocks.contains(indexPattern) }
                            }

                            val isError = remember { mutableStateOf(false) }

                            if (!isAnswered){
                                SimpleTextForPutTask(
                                    text = beforeText
                                )
                                Box(
                                    modifier = Modifier
                                        .height(ProjectTheme.dimens.defaultBlockHeight)
                                        .width(dimens.defaultBlockWidth)
                                        .background(
                                            color = if (isError.value) Color.Transparent
                                                    else palette.contentTertiary,
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
                                                    addIndexAsAnswered(index, indexPattern)
                                                } else {
                                                    isError.value = true
                                                }
                                            }
                                        )
                                )
                                SimpleTextForPutTask(
                                    text = afterText
                                )
                            } else {
                                SimpleTextForPutTask(
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
}