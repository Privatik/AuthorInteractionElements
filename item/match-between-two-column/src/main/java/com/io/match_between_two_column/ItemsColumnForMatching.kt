@file:OptIn(ExperimentalFoundationApi::class)

package com.io.match_between_two_column

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import com.io.core.ui.ProjectTheme
import com.io.item.negativeAnswer

@Composable
internal fun RowScope.ItemsColumnForMatching(
    modifier: Modifier = Modifier,
    items: List<PairMatchedItems>,
    selectedItemIdFromColumn: State<Long?>,
    getMatchedItem: (PairMatchedItems) -> MatchedItem,
    addInBinder: (Long, Int, ElementRect) -> Unit,
    removeFromBinder: (Long) -> Unit,
    matchItems: (currentMatchedItem: MatchedItem?, notCompareWithOtherItem: () -> Unit) -> Unit,
) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        state = state,
    ){
        itemsIndexed(items, key = { _, it -> it.itemFromFirstColumn.id }){ index, item ->
            var negativeAnswer by remember { mutableStateOf(false) }

            MatchItem(
                modifier = Modifier
                    .animateItemPlacement(tween(1000))
                    .zIndex(if (item.isFound) 2f else 1f)
                    .negativeAnswer(
                        doAnimate = negativeAnswer,
                        shape = MaterialTheme.shapes.medium,
                        color = ProjectTheme.palette.error,
                        onFinishedAnimate = { negativeAnswer = false }
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .subscribeOnChangePositionInParental(
                        isSkipItem = !item.isFound,
                        addItem = { rect -> addInBinder(item.itemFromFirstColumn.id, index, rect) },
                        removeItem = { removeFromBinder(item.itemFromFirstColumn.id) }
                    ),
                item = getMatchedItem(item),
                isFound = item.isFound,
                selectedItemId = selectedItemIdFromColumn,
                matchItem = { matchedItem ->
                    matchItems(matchedItem){
                        negativeAnswer = true
                    }
                }
            )

            if (index != items.lastIndex){
                Spacer(modifier = Modifier.height(ProjectTheme.dimens.smallSpace))
            }
        }
    }
}