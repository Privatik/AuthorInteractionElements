package com.io.match_between_two_column

data class PairMatchedItems(
    val itemFromFirstColumn: MatchedItem,
    val itemFromSecondColumn: MatchedItem,
    val isFound: Boolean = false,
)

data class MatchedItem(
    val id: Long,
    val text: String,
)