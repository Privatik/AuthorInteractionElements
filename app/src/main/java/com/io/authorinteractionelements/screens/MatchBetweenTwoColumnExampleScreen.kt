package com.io.authorinteractionelements.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.io.authorinteractionelements.R
import com.io.core.ui.DefaultDimens.heightQuestionBlock
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens
import com.io.match_between_two_column.MatcherBetweenTwoColumn
import com.io.match_between_two_column.PairMatchItems

@Composable
fun MatchBetweenTwoColumnExampleScreen(
    modifier: Modifier = Modifier,
    orderedFirstColumn: List<PairMatchItems>,
    orderedSecondColumn: List<PairMatchItems>,
    foundMatchItems: (Long) -> Unit
) {
    val palette = ProjectTheme.palette
    val border = remember { BorderStroke(2.dp, palette.backgroundPrimary) }

    Card(
        modifier = Modifier
            .padding(dimens.cardOutPadding),
        backgroundColor = palette.backgroundTertiary,
        shape = MaterialTheme.shapes.medium,
        border = border,
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(dimens.insidePadding)
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
                orderedFirstColumn = orderedFirstColumn,
                orderedSecondColumn = orderedSecondColumn,
                weightFirstColumn = 1f,
                weightSecondColumn = 2f,
                foundMatchItems = foundMatchItems
            )
        }
    }
}