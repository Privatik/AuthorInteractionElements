package com.io.survey

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

fun Modifier.verticalScrollbar(
    state: LazyListState,
    width: Dp = 4.dp,
    color: Color,
) = composed {

    val isHasVisibleItems = remember {
        derivedStateOf { state.layoutInfo.visibleItemsInfo.isNotEmpty() }
    }

    val oneChunkHeightScrollBar = remember {
        mutableStateOf(state.layoutInfo.visibleItemsInfo.size)
    }

    val containerSize = remember {
        mutableStateOf(IntSize.Zero)
    }

    LaunchedEffect(isHasVisibleItems.value, containerSize.value){
        oneChunkHeightScrollBar.value = state.layoutInfo.visibleItemsInfo.size
    }

    Modifier
        .onSizeChanged {
            if (it != containerSize.value){
               containerSize.value = it
            }
        }
        .drawWithContent {
        drawContent()

        val firstVisibleElement = state.layoutInfo.visibleItemsInfo.firstOrNull()

        if (firstVisibleElement != null) {
            val chunkHeight = size.height / state.layoutInfo.totalItemsCount

            val scrollbarHeight = (oneChunkHeightScrollBar.value * chunkHeight)

            val maxAvailableOffset = size.height - scrollbarHeight
            val percentageOffset = (-firstVisibleElement.offset.toFloat()/firstVisibleElement.size.toFloat())
            val scrollbarOffsetY = ((firstVisibleElement.index * chunkHeight) + (percentageOffset * chunkHeight))
                .coerceAtMost(maxAvailableOffset)

            if (scrollbarHeight != size.height){
                drawRect(
                    color = color,
                    topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                    size = Size(width.toPx(), scrollbarHeight),
                )
            }
        }
    }
}