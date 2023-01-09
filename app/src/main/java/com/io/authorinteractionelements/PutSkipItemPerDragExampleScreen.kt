package com.io.authorinteractionelements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.io.core.ui.LocalPaletteColors
import com.io.core.ui.ProjectTheme.dimens
import com.io.item.InteractionText
import com.io.put_skip_per_drag.ChangePositionObserver
import com.io.put_skip_per_drag.DragElementsRow
import com.io.put_skip_per_drag.InteractionElement
import com.io.put_skip_per_drag.containerForDraggableItem
import java.util.*

private const val Mock = "1 пишется как ###|dragBlock|Один|###, а 2 пишется как ###|dragBlock|Два|### а 3 пишется как ###|dragBlock|Три|###."

@Composable
fun PutSkipItemPerDragExampleScreen(
    modifier: Modifier = Modifier
){
    val palette = LocalPaletteColors.current

    val interactionElements = remember { mutableListOf<InteractionElement>().toMutableStateList() }
    val changePositionObserver = remember { ChangePositionObserver() }

    val itemDefaultModifier = remember {
        Modifier.height(30.dp)
    }

    Column(
        modifier = modifier
    ) {
        InteractionText(
            modifier = Modifier,
            text = Mock,
            pattern = """###\|\w+\|\w+\|###""",
            getInteractionHelper = { helper ->
                interactionElements.addAll(
                    helper
                        .interactionElements { foundPattern ->
                            foundPattern.split("|")[2].lowercase(Locale.getDefault())
                        }
                        .mapIndexed { index, s -> InteractionElement(id = index,value = s) }
                )
            },
            textPlaceable = { text ->
                Text(
                    modifier = itemDefaultModifier,
                    text = text
                )
            },
            interactionPlaceable = { beforeText, afterText, foundPattern ->
                val rightAnswer = remember(foundPattern) {
                    foundPattern.split("|")[2].lowercase(Locale.getDefault())
                }

                var isAnswered by remember {
                    mutableStateOf(false)
                }

                if (!isAnswered){
                    Text(
                        modifier = itemDefaultModifier,
                        text = beforeText
                    )
                    Box(
                        modifier = itemDefaultModifier
                            .width(50.dp)
                            .background(palette.contentPrimary)
                            .containerForDraggableItem(
                                observer = changePositionObserver,
                                hoveringOnItem = {
                                    if (it.value == rightAnswer) {
                                        interactionElements.remove(it)
                                        isAnswered = true
                                    }
                                }
                            )
                    )
                    Text(
                        modifier = itemDefaultModifier,
                        text = afterText
                    )
                } else {
                    Text(
                        modifier = itemDefaultModifier,
                        text = "$beforeText$rightAnswer$afterText"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(dimens.mediumSpace))
        DragElementsRow(
            modifier = Modifier
                .fillMaxWidth(),
            changePositionObserver = changePositionObserver,
            interactionElements = interactionElements
        )
    }
}