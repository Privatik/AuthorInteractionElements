package com.io.authorinteractionelements.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.io.authorinteractionelements.components.DefaultCard
import com.io.authorinteractionelements.components.HeadForExample
import com.io.authorinteractionelements.R
import com.io.authorinteractionelements.components.SimpleTextForPutTask
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens
import com.io.interaction_text_compose.BodyForInteractionText
import com.io.interaction_text_compose.InteractionText
import com.io.put_skip_per_edit.SkipTextField
import java.util.*

@Composable
fun PutSkipItemPerEditExampleScreen(
    modifier: Modifier = Modifier,
    interactionTextItems: List<BodyForInteractionText>,
    addIndexAsAnswered: (index: Int, indexPattern: Int) -> Unit
){
    val palette = ProjectTheme.palette

    HeadForExample(
        modifier = modifier,
        items = interactionTextItems,
    ) { index, interactionText ->
        DefaultCard {
            Column(
                modifier = Modifier.padding(dimens.insidePadding)
            ) {
                Text(
                    text = stringResource(R.string.put_skip_item),
                    fontSize = dimens.questionFontSize,
                    color = palette.textSecondary,
                )

                Spacer(modifier = Modifier.height(dimens.mediumSpace))

                InteractionText(
                    modifier = Modifier,
                    interactionBody = interactionText,
                    textPlaceable = { text ->
                        SimpleTextForPutTask(text)
                    },
                    interactionPlaceable = { indexPattern, beforeText, afterText, foundPattern ->
                        val dimens = dimens

                        val rightAnswer = remember(foundPattern) {
                            foundPattern.split("|")[2].lowercase(Locale.getDefault())
                        }

                        val isAnswered by remember {
                            derivedStateOf { interactionTextItems[0].indexAnsweredBlocks.contains(indexPattern) }
                        }

                        val inputText = rememberSaveable {
                            mutableStateOf(if (isAnswered) rightAnswer else "")
                        }

                        val isError = remember {
                            mutableStateOf(false)
                        }

                        if (beforeText.isNotBlank()){
                            SimpleTextForPutTask(beforeText)
                        }
                        SkipTextField(
                            inputText = inputText.value,
                            readOnly = isAnswered,
                            onTextChange = {
                                inputText.value = it
                            },
                            isError = isError,
                            onCheckText = {
                                if (rightAnswer == inputText.value.lowercase(Locale.getDefault())){
                                    addIndexAsAnswered(index, indexPattern)
                                } else {
                                    isError.value = true
                                }
                            },
                            modifier = Modifier
                                .then(
                                    if (isAnswered) Modifier.width(IntrinsicSize.Min)
                                    else Modifier.widthIn(min = dimens.defaultBlockWidth)
                                ),
                        )
                        if (afterText.isNotBlank()){
                            SimpleTextForPutTask(afterText)
                        }
                    }
                )
            }
        }
    }
}