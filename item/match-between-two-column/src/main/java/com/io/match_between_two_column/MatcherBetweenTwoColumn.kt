@file:OptIn(ExperimentalFoundationApi::class)

package com.io.match_between_two_column

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import com.io.item.negativeAnswer

@Composable
fun MatcherBetweenTwoColumn(
    modifier: Modifier = Modifier,
    orderedFirstColumn: List<PairMatchItems>,
    orderedSecondColumn: List<PairMatchItems>,
    foundMatchItems: (Long) -> Unit
){
    val itemIdFromFirstColumn = rememberSaveable { mutableStateOf<Long?>(null) }
    val itemIdFromSecondColumn = rememberSaveable { mutableStateOf<Long?>(null) }

    val helper = remember { MatchHelper(foundMatchItems) }

    Row(
        modifier = modifier.height(150.dp)
    ) {
        LazyColumnForMatch(
            items = orderedFirstColumn,
            selectedItemIdFromColumn = itemIdFromFirstColumn,
            getItemFromPair = { it.itemFromFirstColumn },
            selectItem = { item, notCompareWithOtherItem ->
                itemIdFromFirstColumn.value = item?.id
                helper.comparableIds(
                    currentId = itemIdFromFirstColumn,
                    otherId = itemIdFromSecondColumn,
                    notCompareWithOtherItem = notCompareWithOtherItem
                )
            }
        )
        LazyColumnForMatch(
            items = orderedSecondColumn,
            selectedItemIdFromColumn = itemIdFromSecondColumn,
            getItemFromPair = { it.itemFromSecondColumn },
            selectItem = { item, notCompareWithOtherItem ->
                itemIdFromSecondColumn.value = item?.id
                helper.comparableIds(
                    currentId = itemIdFromSecondColumn,
                    otherId = itemIdFromFirstColumn,
                    notCompareWithOtherItem = notCompareWithOtherItem
                )
            }
        )
    }
}

private class MatchHelper(
    private val foundMatchItems: (Long) -> Unit
){
    fun comparableIds(
        currentId: MutableState<Long?>,
        otherId: MutableState<Long?>,
        notCompareWithOtherItem: () -> Unit,
    ){
        comparableIds(
            firstId = currentId.value,
            secondId = otherId.value,
            doIfCompare = {
                foundMatchItems(currentId.value!!)
                currentId.value = null
                otherId.value = null
            },
            doIfNotCompare = {
                notCompareWithOtherItem()
                currentId.value = null
            }
        )
    }

    private inline fun comparableIds(
        firstId: Long?,
        secondId: Long?,
        doIfCompare: () -> Unit,
        doIfNotCompare: () -> Unit,
    ) {
        if (firstId != null && secondId != null){
            if (firstId == secondId){
                doIfCompare()
            } else {
                doIfNotCompare()
            }
        }
    }
}

@Composable
private fun RowScope.LazyColumnForMatch(
    items: List<PairMatchItems>,
    selectedItemIdFromColumn: State<Long?>,
    getItemFromPair: (PairMatchItems) -> ItemColumn,
    selectItem: (item: ItemColumn?, notCompareWithOtherItem: () -> Unit) -> Unit,
){
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = dimens.insidePadding),
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
                        color = palette.error,
                        onFinishedAnimate = { negativeAnswer = false }
                    )
                    .border(
                        width = 1.dp,
                        color = palette.backgroundPrimary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .onGloballyPositioned {
                        println("$index ${getItemFromPair(item).text} ${it.size} ${it.positionInRoot()}")
                    },
                item = getItemFromPair(item),
                isFound = item.isFound,
                selectedItemId = selectedItemIdFromColumn,
                selectItem = {
                    selectItem(it){
                        negativeAnswer = true
                    }
                }
            )
            if (index != items.lastIndex){
                Spacer(modifier = Modifier.height(dimens.smallSpace))
            }
        }
    }
}