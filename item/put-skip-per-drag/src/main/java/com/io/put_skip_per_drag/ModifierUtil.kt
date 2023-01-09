package com.io.put_skip_per_drag

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.plus
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun Modifier.draggableInContainer(
    observer: ChangePositionObserver,
    animationSpec: AnimationSpec<Offset> = spring(),
    item: () -> InteractionElement
) = composed{

    val size = remember { mutableStateOf<IntSize>(IntSize.Zero) }
    val offsetPositionInParental = remember { mutableStateOf<Offset>(Offset.Unspecified) }
    val dragAnimatable = remember {
        Animatable(
            initialValue = Offset.Zero,
            typeConverter = Offset.VectorConverter
        )
    }

    Modifier
        .pointerInput(offsetPositionInParental.value) {

            coroutineScope {
                fun finishDrag() {
                    observer.findMatching(
                        offset = offsetPositionInParental.value
                            .plus(dragAnimatable.value)
                            .plus(
                                IntOffset(
                                    size.value.width / 2,
                                    size.value.height / 2
                                )
                            ),
                        element = item()
                    )
                    launch {
                        dragAnimatable.animateTo(Offset.Zero, animationSpec)
                    }
                }

                detectDragGestures(
                    onDragStart = { _ -> },
                    onDrag = { _, dragAmount ->
                        launch {
                            dragAnimatable.snapTo(
                                dragAnimatable.value.plus(dragAmount)
                            )
                        }
                    },
                    onDragEnd = { finishDrag() },
                    onDragCancel = { finishDrag() }
                )
            }
        }
        .onGloballyPositioned {
            if (offsetPositionInParental.value != it.positionInRoot()) {
                offsetPositionInParental.value = it.positionInRoot()
            }
            if (size.value != it.size) {
                size.value = it.size
            }
        }
        .offset {
            IntOffset(
                dragAnimatable.value.x.toInt(),
                dragAnimatable.value.y.toInt()
            )
        }
}

fun Modifier.containerForDraggableItem(
    observer: ChangePositionObserver,
    hoveringOnItem: (element: InteractionElement) -> Unit
) = composed {
    var interactionRect by remember {
        mutableStateOf(
            InteractionRect(
                Offset.Zero,
                IntSize.Zero,
            )
        )
    }

    DisposableEffect(interactionRect){
        val positionConfig = PositionConfig(interactionRect){
            hoveringOnItem(it)
        }

        observer.subscribe(positionConfig)

        onDispose { observer.unsubscribe(positionConfig) }
    }

    Modifier
        .onGloballyPositioned {
            if (interactionRect.offset != it.positionInRoot()) {
                interactionRect =
                    InteractionRect(
                        offset = it.positionInRoot(),
                        size = it.size
                    )
            }
        }
}