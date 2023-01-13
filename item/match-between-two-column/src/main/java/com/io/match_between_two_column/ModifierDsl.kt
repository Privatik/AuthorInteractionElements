package com.io.match_between_two_column

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent

internal fun Modifier.subscribeOnChangePositionInParental(
    isSkipItem: Boolean,
    addItem: (ElementRect) -> Unit,
    removeItem: () -> Unit,
) = composed {
    var elementRect by remember { mutableStateOf<ElementRect?>(null) }

    DisposableEffect(isSkipItem, elementRect){

        elementRect?.also{ rect ->
            if (!isSkipItem){
                addItem(rect)
            }
        }

        onDispose {
            elementRect?.also {
                removeItem()
            }
        }
    }

    Modifier
        .onGloballyPositioned {
            val newElementRect = ElementRect(it.positionInParent(), it.size)
            if (newElementRect != elementRect) {
                elementRect = newElementRect
            }
        }
}