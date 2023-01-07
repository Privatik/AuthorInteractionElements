package com.io.authorinteractionelements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.io.item.InteractionText

private const val Mock = "1 пишется как ###|textField|Один|###, а 2 пишется как ###|textField|Два|###, а 3 пишется как ###|textField|Три|###."

@Composable
fun PutSkipItemExampleScreen(
    modifier: Modifier = Modifier
){
    InteractionText(
        modifier = modifier,
        text = Mock,
        pattern = """###\|\w+\|\w+\|###""",
        textPlaceable = { currentWidth, maxWidth, text ->

        },
        interactionPlaceable = { currentWidth, maxWidth, foundPattern ->

        }
    )
}