package com.io.authorinteractionelements.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.io.authorinteractionelements.components.DefaultCard
import com.io.authorinteractionelements.components.HeadForExample
import com.io.authorinteractionelements.R
import com.io.core.ui.DefaultDimens.heightQuestionBlock
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import com.io.match_between_two_column.MatcherBetweenTwoColumn
import com.io.match_between_two_column.PairMatchedItems

data class PairOrderedColumn(
    val orderedFirstColumn: List<PairMatchedItems>,
    val orderedSecondColumn: List<PairMatchedItems>,
)

@Composable
fun MatchBetweenTwoColumnExampleScreen(
    modifier: Modifier = Modifier,
    pairsOrderedColumn: List<PairOrderedColumn>,
    foundMatchItems: (Int, Long) -> Unit
) {

    HeadForExample(
        modifier = modifier,
        items = pairsOrderedColumn
    ) { index, item ->
        DefaultCard {
            Column(
                modifier = Modifier
                    .padding(dimens.insidePadding)
            ) {
                Text(
                    text = stringResource(R.string.match_elements),
                    fontSize = dimens.questionFontSize,
                    color = palette.textSecondary,
                )

                Spacer(modifier = Modifier.height(dimens.mediumSpace))

                MatcherBetweenTwoColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightQuestionBlock),
                    orderedFirstColumn = item.orderedFirstColumn,
                    orderedSecondColumn = item.orderedSecondColumn,
                    weightFirstColumn = 1f,
                    weightSecondColumn = 2f,
                    foundMatchItems = { id ->
                        foundMatchItems(index, id)
                    }
                )
            }
        }
    }
}