package com.io.survey

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.io.core.ui.ProjectTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun QuestionPager(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    userScrollEnabled: Boolean,
    content: LazyListScope.() -> Unit,
){
    var width by remember { mutableStateOf(0) }

    Canvas(
        modifier = Modifier.fillMaxWidth(),
    ) {
        width = size.width.roundToInt()
    }

    LaunchedEffect(Unit){
        scrollState.interactionSource
            .interactions
            .collect{
                when (it){
                    is DragInteraction.Cancel,
                    is DragInteraction.Stop -> {
                        scrollState.finishScroll(this, width)
                    }
                }
            }
    }

    LazyRow(
        modifier = modifier,
        state = scrollState,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}