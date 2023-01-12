package com.io.authorinteractionelements.mock

import com.io.match_between_two_column.ItemColumn
import com.io.match_between_two_column.PairMatchItems

val mockMatchItems = mockQuestionToAnswer.mapIndexed { index, pair ->
    PairMatchItems(
        ItemColumn(index.toLong(), pair.first),
        ItemColumn(index.toLong(), pair.second),
    )
}

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