package com.io.authorinteractionelements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.io.match_between_two_column.ItemColumn
import com.io.match_between_two_column.MatcherBetweenTwoColumn
import com.io.match_between_two_column.PairMatchItems

@Composable
fun MatchBetweenTwoColumnExampleScreen(
    modifier: Modifier = Modifier,
    orderedFirstColumn: List<PairMatchItems>,
    orderedSecondColumn: List<PairMatchItems>,
    matchItems: (Long?, Long?) -> Unit
) {

    MatcherBetweenTwoColumn(
        modifier = modifier,
        orderedFirstColumn = orderedFirstColumn,
        orderedSecondColumn = orderedSecondColumn,
        matchItems = matchItems
    )
}