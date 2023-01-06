package com.io.authorinteractionelements

import com.io.match_between_two_column.ItemColumn
import com.io.match_between_two_column.PairMatchItems

val mockMatchItems = listOf<PairMatchItems>(
    PairMatchItems(
        ItemColumn(1, "1"),
        ItemColumn(1, "Один")
    ),
    PairMatchItems(
        ItemColumn(2, "2"),
        ItemColumn(2, "Два")
    ),
    PairMatchItems(
        ItemColumn(3, "3"),
        ItemColumn(3, "Три")
    ),
)

fun List<PairMatchItems>.randomOrdered(): Pair<List<PairMatchItems>, List<PairMatchItems>>{
    val firstOrderedList = map { it.itemFromFirstColumn.id }
        .shuffled()
        .withIndex()
        .associate { it.value to it.index }
        .let {
            Array<PairMatchItems?>(it.size) { null }
                .apply {
                    this@randomOrdered.forEach { item ->
                        this[it.getValue(item.itemFromFirstColumn.id)] = item
                    }
                }
                .mapNotNull { body -> body }
        }

    val secondOrderedList = map { it.itemFromSecondColumn.id }
        .shuffled()
        .withIndex()
        .associate { it.value to it.index }
        .let {
            Array<PairMatchItems?>(it.size) { null }
                .apply {
                    this@randomOrdered.forEach { item ->
                        this[it.getValue(item.itemFromSecondColumn.id)] = item
                    }
                }
                .mapNotNull { body -> body }
        }


    return firstOrderedList to secondOrderedList
}