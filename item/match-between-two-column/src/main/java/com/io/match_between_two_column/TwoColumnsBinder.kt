package com.io.match_between_two_column

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Path

internal class TwoColumnsBinder {
    private var firstColumnElementRect: ElementRect? by mutableStateOf<ElementRect?>(null)
    private var secondColumnElementRect: ElementRect? by mutableStateOf<ElementRect?>(null)

    private val foundedItemsFromFirstColumn = SnapshotStateMap<Long, Pair<Int,ElementRect>>()
    private val foundedItemsFromSecondColumn = SnapshotStateMap<Long, Pair<Int,ElementRect>>()
    private val path = Path()

    fun updateFirstColumnRect(rect: ElementRect){
        if (rect != firstColumnElementRect){
            firstColumnElementRect = rect
        }
    }

    fun updateSecondColumnRect(rect: ElementRect){
        if (rect != secondColumnElementRect){
            secondColumnElementRect = rect
        }
    }
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
        foundedItemsFromSecondColumn[itemId] = index to elementRect
    }

    fun removeFoundedVisibleItemFromFirstColumn(
        itemId: Long
    ) {
        foundedItemsFromFirstColumn.remove(itemId)
    }

    fun removeFoundedVisibleItemFromSecondColumn(
        itemId: Long
    ) {
        foundedItemsFromSecondColumn.remove(itemId)
    }

    fun getBindPath(): Path {
        path.reset()
        if (firstColumnElementRect != null && secondColumnElementRect != null) {
            val madeBinds = hashSetOf<Long>()

            val minIndexFirstColumn = foundedItemsFromFirstColumn
                .minOfOrNull { it.value.first } ?: Int.MAX_VALUE
            val minIndexSecondColumn = foundedItemsFromSecondColumn
                .minOfOrNull { it.value.first } ?: Int.MAX_VALUE

            foundedItemsFromFirstColumn.forEach { (id, pair) ->
                val (indexFromFirstColumn, firstElementRect) = pair

                path.moveTo(
                    x = firstColumnElementRect!!.offset.x + firstElementRect.offset.x
                            + firstElementRect.size.width,
                    y = firstColumnElementRect!!.offset.y + firstElementRect.offset.y
                            + (firstElementRect.size.height / 2)
                )

                if (foundedItemsFromSecondColumn.contains(id)){
                    val ( _ , secondElementRect) = foundedItemsFromSecondColumn.getValue(id)
                    path.lineTo(
                        x = secondColumnElementRect!!.offset.x + secondElementRect.offset.x,
                        y = secondColumnElementRect!!.offset.y + secondElementRect.offset.y
                                + (secondElementRect.size.height / 2),
                    )
                } else if (indexFromFirstColumn > minIndexSecondColumn){
                    path.lineTo(
                        x = secondColumnElementRect!!.offset.x,
                        y = secondColumnElementRect!!.offset.y
                                + secondColumnElementRect!!.size.height,
                    )
                } else {
                    path.lineTo(
                        x = secondColumnElementRect!!.offset.x,
                        y = secondColumnElementRect!!.offset.y,
                    )
                }

                madeBinds.add(id)
            }

            foundedItemsFromSecondColumn.forEach { (id, pair) ->
                if (madeBinds.contains(id)) return@forEach

                val (indexFromSecondColumn, secondElementRect) = pair

                path.moveTo(
                    x = secondColumnElementRect!!.offset.x + secondElementRect.offset.x,
                    y = secondColumnElementRect!!.offset.y + secondElementRect.offset.y
                            + (secondElementRect.size.height / 2)
                )

                if (indexFromSecondColumn > minIndexFirstColumn){
                    path.lineTo(
                        x = firstColumnElementRect!!.offset.x + firstColumnElementRect!!.size.width,
                        y = firstColumnElementRect!!.offset.y + firstColumnElementRect!!.size.height,
                    )
                } else {
                    path.lineTo(
                        x = firstColumnElementRect!!.offset.x + firstColumnElementRect!!.size.width,
                        y = firstColumnElementRect!!.offset.y,
                    )
                }
            }
        }
        return path
    }
}