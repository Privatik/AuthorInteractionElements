package com.io.put_skip_per_drag

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragElementsRow(
    modifier: Modifier = Modifier,
    changePositionObserver: ChangePositionObserver,
    interactionElements: List<InteractionElement>,
){
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(dimens.insidePadding)
    ){
        items(interactionElements, key = { it.id }) { item ->

            DragItem(
                modifier = Modifier
                    .animateItemPlacement()
                    .draggableInContainer(
                        observer = changePositionObserver,
                        item = { item }
                    )
                    .padding(dimens.insidePadding)
                    .background(palette.contentPrimary, shapes.medium),
                item = item,
            )
        }
    }
}