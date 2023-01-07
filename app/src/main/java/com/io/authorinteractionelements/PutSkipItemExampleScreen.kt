package com.io.authorinteractionelements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.io.item.InteractionText
import com.io.put_skip_per_edit.SkipTextField
import java.util.*

private const val Mock = "1 пишется как ###|textField|Один|### а 2 пишется как ###|textField|Два|### а 3 пишется как ###|textField|Три|###."

@Composable
fun PutSkipItemExampleScreen(
    modifier: Modifier = Modifier
){
    InteractionText(
        modifier = modifier,
        text = Mock,
        pattern = """###\|\w+\|\w+\|###""",
        textPlaceable = { text ->
            Text(
                modifier = Modifier
                    .height(30.dp),
                text = text
            )
        },
        interactionPlaceable = { foundPattern ->
            val rightAnswer = remember(foundPattern) {
                foundPattern.split("|")[2].lowercase(Locale.getDefault())
            }
            val inputText = rememberSaveable {
                mutableStateOf("")
            }
            var isAnswered by remember {
                mutableStateOf(false)
            }

            if (!isAnswered){
                SkipTextField(
                    inputText = inputText.value,
                    onTextChange = {
                        inputText.value = it
                    },
                    onCheckText = {
                        if (rightAnswer == inputText.value.lowercase(Locale.getDefault())){
                            isAnswered = true
                        }
                    },
                    modifier = Modifier
                        .wrapContentWidth()
                        .widthIn(min = 10.dp)
                        .height(30.dp),
                )
            } else {
                Text(
                    modifier = Modifier
                        .height(30.dp),
                    text = rightAnswer
                )
            }
        }
    )
}