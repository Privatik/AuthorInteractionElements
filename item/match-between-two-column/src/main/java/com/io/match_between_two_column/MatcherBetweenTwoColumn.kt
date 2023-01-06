package com.io.match_between_two_column

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import com.io.item.changeBackgroundPerRipple

@Composable
fun MatcherBetweenTwoColumn(
    modifier: Modifier = Modifier,
    orderedFirstColumn: List<PairMatchItems>,
    orderedSecondColumn: List<PairMatchItems>,
    matchItems: (Long?, Long?) -> Unit
){
    val itemIdFromFirstColumn = rememberSaveable { mutableStateOf<Long?>(null) }
    val itemIdFromSecondColumn = rememberSaveable { mutableStateOf<Long?>(null) }

    Row(
        modifier = modifier.height(dimens.maxBlockSize)
    ) {
        LazyItems(
            items = orderedFirstColumn,
            selectedItemIdFromColumn = itemIdFromFirstColumn,
            getItemFromPair = {
                it.itemFromFirstColumn
            },
            changeSelectItem = {
                itemIdFromFirstColumn.value = it.id
                doIfNotHaveNullItems(itemIdFromFirstColumn.value, itemIdFromSecondColumn.value){
                    matchItems(itemIdFromFirstColumn.value, itemIdFromSecondColumn.value)
                }
            }
        )
        LazyItems(
            items = orderedSecondColumn,
            selectedItemIdFromColumn = itemIdFromSecondColumn,
            getItemFromPair = {
                it.itemFromSecondColumn
            },
            changeSelectItem = {
                itemIdFromSecondColumn.value = it.id
                println("match ${itemIdFromFirstColumn.value} ${itemIdFromSecondColumn.value}")
                doIfNotHaveNullItems(itemIdFromFirstColumn.value, itemIdFromSecondColumn.value){
                    matchItems(itemIdFromFirstColumn.value, itemIdFromSecondColumn.value)
                }
            }
        )
    }
}

private fun doIfNotHaveNullItems(
    vararg items: Any?,
    block: () -> Unit
){
    if (items.all { it != null }){
        block()
    }
}

@Composable
private fun RowScope.LazyItems(
    items: List<PairMatchItems>,
    selectedItemIdFromColumn: State<Long?>,
    getItemFromPair: (PairMatchItems) -> ItemColumn,
    changeSelectItem: (ItemColumn) -> Unit,
){
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = dimens.insidePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        itemsIndexed(items, key = { _, it -> it.itemFromFirstColumn.id }){ index, item ->
            MatchItem(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .border(
                        width = 1.dp,
                        color = palette.backgroundPrimary,
                        shape = MaterialTheme.shapes.medium
                    ),
                item = getItemFromPair(item),
                isFound = item.isFound,
                selectedItemId = selectedItemIdFromColumn,
                changeSelectItem = changeSelectItem
            )
            if (index != items.lastIndex){
                Spacer(modifier = Modifier.height(dimens.smallSpace))
            }
        }
    }
}