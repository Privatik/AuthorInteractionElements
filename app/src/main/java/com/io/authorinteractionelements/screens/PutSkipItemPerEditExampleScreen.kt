package com.io.authorinteractionelements.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.io.authorinteractionelements.R
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import com.io.item.BodyForInteractionText
import com.io.item.InteractionText
import com.io.put_skip_per_edit.SkipTextField
import java.util.*

private const val Mock = "1 пишется как \"###|textField|Один|###\", а 2 пишется как \"###|textField|Два|###\", а 3 пишется как \"###|textField|Три|###\"."

@Composable
fun PutSkipItemPerEditExampleScreen(
    modifier: Modifier = Modifier,
    interactionItems: List<BodyForInteractionText>,
    addIndexAsAnswered: (Int) -> Unit
){
    val palette = ProjectTheme.palette
    val border = remember { BorderStroke(2.dp, palette.backgroundPrimary) }

    Card(
        modifier = Modifier
            .padding(dimens.cardOutPadding),
        backgroundColor = palette.backgroundTertiary,
        shape = MaterialTheme.shapes.medium,
        border = border,
        elevation = 3.dp
    ) {
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
                interactionBody = interactionItems[0],
                textPlaceable = { text ->
                    TextForPutSkipEdit(text)
                },
                interactionPlaceable = { index, beforeText, afterText, foundPattern ->
                    val dimens = dimens
                    val rightAnswer = remember(foundPattern) {
                        foundPattern.split("|")[2].lowercase(Locale.getDefault())
                    }
                    val inputText = rememberSaveable {
                        mutableStateOf("")
                    }
                    val isAnswered by remember {
                        derivedStateOf { interactionItems[0].indexAnsweredBlocks.contains(index) }
                    }
                    val isError = remember {
                        mutableStateOf(false)
                    }

                    if (beforeText.isNotBlank()){
                        TextForPutSkipEdit(beforeText)
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
                                addIndexAsAnswered(index)
                            } else {
                                isError.value = true
                            }
                        },
                        modifier = Modifier
                            .widthIn(min = dimens.defaultBlockWidth),
                    )
                    if (afterText.isNotBlank()){
                        TextForPutSkipEdit(afterText)
                    }
                }
            )
        }
    }
}

@Composable
private fun TextForPutSkipEdit(
    text: String
){
    Text(
        modifier = Modifier.height(dimens.defaultBlockHeight),
        text = text,
        fontSize = dimens.justVariationFontSize,
        color = palette.textSecondary
    )
}