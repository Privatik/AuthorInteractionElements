package com.io.survey

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun LazyListState.finishScroll(scope: CoroutineScope, widthItem: Int){
    scope.launch {
        if (firstVisibleItemScrollOffset >= widthItem / 2){
            animateScrollToItem(firstVisibleItemIndex + 1)
        } else {
            animateScrollToItem(firstVisibleItemIndex)
        }
    }
}