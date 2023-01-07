package com.io.authorinteractionelements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette

@Composable
fun ButtonsScreen(
    modifier: Modifier,
    changeScreen: (Screen) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = palette.backgroundPrimary
            ),
            onClick = {
                changeScreen(Screen.Survey)
            },
        ) {
            Text(
                text = stringResource(R.string.multi_survey),
                color = palette.contentPrimary
            )
        }
        Spacer(modifier = Modifier.height(dimens.smallSpace))
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = palette.backgroundPrimary
            ),
            onClick = {
                changeScreen(Screen.MatchBetweenTwoColumn)
            },
        ) {
            Text(
                text = stringResource(R.string.match_between_two_column),
                color = palette.contentPrimary
            )
        }
        Spacer(modifier = Modifier.height(dimens.smallSpace))
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = palette.backgroundPrimary
            ),
            onClick = {
                changeScreen(Screen.PutSkipItem)
            },
        ) {
            Text(
                text = stringResource(R.string.put_skip_item),
                color = palette.contentPrimary
            )
        }
    }
}