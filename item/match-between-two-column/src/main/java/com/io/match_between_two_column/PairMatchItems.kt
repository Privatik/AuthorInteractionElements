package com.io.match_between_two_column

data class PairMatchItems(
    val itemFromFirstColumn: ItemColumn,
    val itemFromSecondColumn: ItemColumn,
    val isFound: Boolean = false,
)

data class ItemColumn(
    val id: Long,
    val text: String,
)