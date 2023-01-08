package com.io.authorinteractionelements

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.io.core.ui.LocalPaletteColors
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import com.io.item.InteractionText
import com.io.put_skip_per_drag.DragItem
import com.io.put_skip_per_drag.InteractionElement
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

private const val Mock = "1 пишется как ###|dragBlock|Один|### а 2 пишется как ###|dragBlock|Два|### а 3 пишется как ###|dragBlock|Три|###."

@Composable
fun PutSkipItemPerDragExampleScreen(
    modifier: Modifier = Modifier
){
    val palette = LocalPaletteColors.current
    val shapes = MaterialTheme.shapes

    val interactionElements = remember { mutableStateListOf<InteractionElement>() }
    Box(
        modifier = modifier
    ) {
        val itemModifier = remember {
            Modifier
                .padding(5.dp)
                .background(palette.contentPrimary, shapes.medium)
        }

        var dragElement by remember {
            mutableStateOf<MoveElement?>(null)
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            InteractionText(
                text = Mock,
                pattern = """###\|\w+\|\w+\|###""",
                getHelper = { helper ->
                    interactionElements.addAll(
                        helper
                            .interactionElements { foundPattern ->
                                foundPattern.split("|")[2].lowercase(Locale.getDefault())
                            }
                            .mapIndexed { index, s ->  InteractionElement(id = index,value = s) }
                    )
                },
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


                    var isAnswered by remember {
                        mutableStateOf(false)

                    }

                    if (!isAnswered){
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(30.dp)
                                .background(palette.contentPrimary)
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
            Spacer(modifier = Modifier.height(dimens.mediumSpace))
            LazyRow(
                contentPadding = PaddingValues(8.dp)
            ){
                items(interactionElements, key = { it.id }) { item ->
                    val offsetPosition = remember {
                        mutableStateOf<Offset>(Offset.Unspecified)
                    }

                    DragItem(
                        modifier = itemModifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        val offset = offsetPosition.value

                                        dragElement = MoveElement(
                                            initX = offset.x,
                                            initY = offset.y,
                                            x = offset.x,
                                            y = offset.y,
                                            item = item,
                                        )
                                    }
                                )
                            }
                            .onGloballyPositioned { layoutCoordinates ->
                                offsetPosition.value = layoutCoordinates.positionInRoot()
                            },
                        item = item,
                    )
                }
            }
        }

        if (dragElement != null){
            val element = remember { dragElement!!.item }

            DragItem(
                modifier = itemModifier
                    .pointerInput(Unit) {

                        coroutineScope {
                            fun finishDrag() {
                                val xOffset = Animatable(dragElement!!.x)
                                val yOffset = Animatable(dragElement!!.y)

                                launch {
                                    coroutineScope {
                                        launch {
                                            xOffset.animateTo(dragElement!!.initX) {
                                                dragElement = dragElement!!.copy(
                                                    x = value
                                                )
                                            }
                                        }
                                        launch {
                                            yOffset.animateTo(dragElement!!.initY) {
                                                dragElement = dragElement!!.copy(
                                                    y = value
                                                )
                                            }
                                        }
                                    }
                                    dragElement = null
                                }
                            }

                            detectDragGestures(
                                onDragStart = { _ -> },
                                onDrag = { _, dragAmount ->
                                    launch {
                                        dragElement = dragElement!!.copy(
                                            x = dragElement!!.x + dragAmount.x,
                                            y = dragElement!!.y + dragAmount.y
                                        )
                                    }
                                },
                                onDragEnd = { finishDrag() },
                                onDragCancel = { finishDrag() }
                            )
                        }
                    }
                    .offset(
                        x = dragElement!!.x.dp,
                        y = dragElement!!.y.dp
                    ),
                item = element,
            )
        }
    }
}

private data class MoveElement(
    val initX: Float,
    val initY: Float,
    val x: Float,
    val y: Float,
    val item: InteractionElement,
)