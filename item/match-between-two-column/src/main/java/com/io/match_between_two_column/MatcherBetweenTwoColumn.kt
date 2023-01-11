@file:OptIn(ExperimentalFoundationApi::class)

package com.io.match_between_two_column

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import com.io.item.negativeAnswer
import kotlin.properties.Delegates

@Composable
fun MatcherBetweenTwoColumn(
    modifier: Modifier = Modifier,
    orderedFirstColumn: List<PairMatchItems>,
    orderedSecondColumn: List<PairMatchItems>,
    foundMatchItems: (Long) -> Unit
){
    val palette = palette
    val itemIdFromFirstColumn = rememberSaveable { mutableStateOf<Long?>(null) }
    val itemIdFromSecondColumn = rememberSaveable { mutableStateOf<Long?>(null) }

    val helper = remember { MatchHelper(foundMatchItems) }
    val binderHelper = remember { BindHelper() }

    Box(
        modifier = modifier.height(150.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .zIndex(2f)
        ) {
            LazyColumnForMatch(
                items = orderedFirstColumn,
                selectedItemIdFromColumn = itemIdFromFirstColumn,
                getItemFromPair = { it.itemFromFirstColumn },
                provideElementRect = {
                    binderHelper.firstColumnElementRect = it
                },
                provideSuccessItem = binderHelper::addFoundedVisibleItemToFirstColumn,
                removeSuccessItem = binderHelper::removeFoundedVisibleItemToFirstColumn,
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
                provideElementRect = {
                    binderHelper.secondColumnElementRect = it
                },
                provideSuccessItem = binderHelper::addFoundedVisibleItemToSecondColumn,
                removeSuccessItem = binderHelper::removeFoundedVisibleItemToSecondColumn,
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

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(5f)
        ){
            val path = binderHelper.getBindPath()
            drawPath(
                path,
                palette.success,
                style = Stroke(
                    width = 4f
                )
            )
        }
    }
}

private data class ElementRect(
    val offset: Offset,
    val size: IntSize,
)

private class BindHelper {
    var firstColumnElementRect: ElementRect by Delegates.notNull()
    var secondColumnElementRect: ElementRect by Delegates.notNull()

    private val foundedItemsFromFirstColumn = SnapshotStateMap<Long, Pair<Int,ElementRect>>()
    private val foundedItemsFromSecondColumn = SnapshotStateMap<Long, Pair<Int,ElementRect>>()
    private val path = Path()

    fun addFoundedVisibleItemToFirstColumn(
        itemId: Long,
        index: Int,
        elementRect: ElementRect
    ) {
        foundedItemsFromFirstColumn[itemId] = index to elementRect
    }

    fun addFoundedVisibleItemToSecondColumn(
        itemId: Long,
        index: Int,
        elementRect: ElementRect
    ) {
        foundedItemsFromFirstColumn[itemId] = index to elementRect
    }

    fun removeFoundedVisibleItemToFirstColumn(
        itemId: Long
    ) {
        foundedItemsFromFirstColumn.remove(itemId)
    }

    fun removeFoundedVisibleItemToSecondColumn(
        itemId: Long
    ) {
        foundedItemsFromFirstColumn.remove(itemId)
    }

    fun getBindPath(): Path{
        println("Path create")
        val madeBinds = hashSetOf<Long>()

        val minIndexFirstColumn = foundedItemsFromFirstColumn.minOfOrNull { it.value.first } ?: 0
        val minIndexSecondColumn = foundedItemsFromFirstColumn.minOfOrNull { it.value.first } ?: 0
        val isFirstColumnAbove = minIndexFirstColumn <= minIndexSecondColumn

        path.reset()
        foundedItemsFromFirstColumn.forEach { (id, pair) ->
            val (_, firstElementRect) = pair

            path.moveTo(
                x = firstElementRect.offset.x + firstElementRect.size.width,
                y = firstElementRect.offset.y + (firstElementRect.size.height / 2)
            )

            println("Path move")

            if (foundedItemsFromSecondColumn.contains(id)){
                val ( _ , secondElementRect) = foundedItemsFromSecondColumn.getValue(id)
                path.lineTo(
                    x = secondElementRect.offset.x,
                    y = secondElementRect.offset.y + (secondElementRect.size.height / 2),
                )
            } else if (isFirstColumnAbove){
                path.lineTo(
                    x = secondColumnElementRect.offset.x,
                    y = secondColumnElementRect.offset.y,
                )
            } else {
                path.lineTo(
                    x = secondColumnElementRect.offset.x,
                    y = secondColumnElementRect.offset.y + secondColumnElementRect.size.height,
                )
            }

            madeBinds.add(id)
        }

        foundedItemsFromSecondColumn.forEach { (id, pair) ->
            if (madeBinds.contains(id)) return@forEach

            val (_, secondElementRect) = pair

            println("Path move")
            path.moveTo(
                x = secondElementRect.offset.x + secondElementRect.size.width,
                y = secondElementRect.offset.y + (secondElementRect.size.height / 2)
            )

            if (isFirstColumnAbove){
                path.lineTo(
                    x = firstColumnElementRect.offset.x + firstColumnElementRect.size.width,
                    y = firstColumnElementRect.offset.y + firstColumnElementRect.size.height,
                )
            } else {
                path.lineTo(
                    x = secondColumnElementRect.offset.x + firstColumnElementRect.size.width,
                    y = secondColumnElementRect.offset.y,
                )
            }
        }

        return path
    }

}

private class MatchHelper(
    private val foundMatchItems: (Long) -> Unit,
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
    provideElementRect: (ElementRect) -> Unit,
    provideSuccessItem: (Long, Int, ElementRect) -> Unit,
    removeSuccessItem: (Long) -> Unit,
    selectItem: (item: ItemColumn?, notCompareWithOtherItem: () -> Unit) -> Unit,
){
    val state = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .padding(dimens.insidePadding)
            .onGloballyPositioned {
                provideElementRect(
                    ElementRect(
                        it.positionInRoot(),
                        it.size
                    )
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        state = state,
    ){
        itemsIndexed(items, key = { _, it -> it.itemFromFirstColumn.id }){ index, item ->
            var negativeAnswer by remember { mutableStateOf(false) }
            var elementRect by remember { mutableStateOf<ElementRect?>(null) }

            DisposableEffect(item.isFound, elementRect){

                elementRect?.also{
                    if (item.isFound){
                        provideSuccessItem(
                            item.itemFromFirstColumn.id,
                            index,
                            it
                        )
                    }
                }

                onDispose {
                    elementRect?.also {
                        removeSuccessItem(item.itemFromFirstColumn.id)
                    }
                }
            }

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
                    .clip(MaterialTheme.shapes.medium)
                    .onGloballyPositioned {
                        val newElementRect = ElementRect(it.positionInRoot(), it.size)
                        if (newElementRect != elementRect){
                            elementRect = newElementRect
                        }
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