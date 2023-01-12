package com.io.put_skip_per_drag

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragElementsRow(
    modifier: Modifier = Modifier,
    changePositionObserver: ChangePositionObserver,
    interactionElements: List<InteractionElement>,
    offsetYForDragItems: State<Dp>,
){
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(dimens.insidePadding)
    ){
        items(interactionElements, key = { it.id }) { item ->

            DragItem(
                modifier = Modifier
                    .padding(top = offsetYForDragItems.value)
                    .animateItemPlacement()
                    .draggableInContainer(
                        observer = changePositionObserver,
                        item = { item }
                    )
                    .padding(dimens.insidePadding)
                    .background(palette.contentPrimary, shapes.medium)
                    .border(
                        width = 1.dp,
                        color = palette.contentSecondary,
                        shape = shapes.medium
                    ),
                item = item,
            )
        }
    }
}