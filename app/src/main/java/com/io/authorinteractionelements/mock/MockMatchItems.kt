package com.io.authorinteractionelements.mock

import com.io.match_between_two_column.MatchedItem
import com.io.match_between_two_column.PairMatchedItems

val mockMatchItems = mockQuestionToAnswer.mapIndexed { index, pair ->
    PairMatchedItems(
        MatchedItem(index.toLong(), pair.first),
        MatchedItem(index.toLong(), pair.second),
    )
}

fun List<PairMatchedItems>.randomOrdered(): Pair<List<PairMatchedItems>, List<PairMatchedItems>>{
    val firstOrderedList = map { it.itemFromFirstColumn.id }
        .shuffled()
        .withIndex()
        .associate { it.value to it.index }
        .let {
            Array<PairMatchedItems?>(it.size) { null }
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
            Array<PairMatchedItems?>(it.size) { null }
                .apply {
                    this@randomOrdered.forEach { item ->
                        this[it.getValue(item.itemFromSecondColumn.id)] = item
                    }
                }
                .mapNotNull { body -> body }
        }


    return firstOrderedList to secondOrderedList
}