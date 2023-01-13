@file:OptIn(ExperimentalFoundationApi::class)

package com.io.match_between_two_column

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import com.io.core.ui.ProjectTheme.palette

@Composable
fun MatcherBetweenTwoColumn(
    modifier: Modifier = Modifier,
    orderedFirstColumn: List<PairMatchedItems>,
    orderedSecondColumn: List<PairMatchedItems>,
    weightFirstColumn: Float = 1f,
    weightSecondColumn: Float = 1f,
    foundMatchItems: (Long) -> Unit
){
    val palette = palette
    val itemIdFromFirstColumn = rememberSaveable { mutableStateOf<Long?>(null) }
    val itemIdFromSecondColumn = rememberSaveable { mutableStateOf<Long?>(null) }

    val comparableHelper = remember { ComparableIdsBetweenTwoColumnHelper(foundMatchItems) }
    val twoColumnsBinder = remember { TwoColumnsBinder() }

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .drawBehind {
                    val path = twoColumnsBinder.getBindPath()
                    drawPath(
                        path,
                        palette.success,
                        style = Stroke(width = 4f)
                    )
                }
        ) {
            ItemsColumnForMatching(
                modifier = Modifier
                    .weight(weightFirstColumn)
                    .onGloballyPositioned { coordination ->
                        twoColumnsBinder.updateFirstColumnRect(
                            ElementRect(
                                coordination.positionInParent(),
                                coordination.size
                            )
                        )
                    },
                items = orderedFirstColumn,
                selectedItemIdFromColumn = itemIdFromFirstColumn,
                getMatchedItem = { it.itemFromFirstColumn },
                addInBinder = twoColumnsBinder::addFoundedVisibleItemToFirstColumn,
                removeFromBinder = twoColumnsBinder::removeFoundedVisibleItemFromFirstColumn,
                matchItems = { currentItem, notCompareWithOtherItem ->
                    itemIdFromFirstColumn.value = currentItem?.id
                    comparableHelper.comparable(
                        currentId = itemIdFromFirstColumn,
                        otherId = itemIdFromSecondColumn,
                        notCompareWithOtherItem = notCompareWithOtherItem
                    )
                }
            )
            ItemsColumnForMatching(
                modifier = Modifier
                    .weight(weightSecondColumn)
                    .onGloballyPositioned { coordination ->
                        twoColumnsBinder.updateSecondColumnRect(
                            ElementRect(
                                coordination.positionInParent(),
                                coordination.size
                            )
                        )
                    },
                items = orderedSecondColumn,
                selectedItemIdFromColumn = itemIdFromSecondColumn,
                getMatchedItem = { it.itemFromSecondColumn },
                addInBinder = twoColumnsBinder::addFoundedVisibleItemToSecondColumn,
                removeFromBinder = twoColumnsBinder::removeFoundedVisibleItemFromSecondColumn,
                matchItems = { currentItem, notCompareWithOtherItem ->
                    itemIdFromSecondColumn.value = currentItem?.id
                    comparableHelper.comparable(
                        currentId = itemIdFromSecondColumn,
                        otherId = itemIdFromFirstColumn,
                        notCompareWithOtherItem = notCompareWithOtherItem
                    )
                }
            )
        }
    }
}