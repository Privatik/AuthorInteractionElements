package com.io.survey

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.io.core.ui.LocalPaletteColors
import com.io.core.ui.ProjectTheme.dimens
import com.io.item.changeBackgroundPerRipple

@Composable
fun SurveyAnswer(
    modifier: Modifier = Modifier,
    isYouAnswered: Boolean,
    selectedSurveyAnswerId: State<Long?>,
    rightSurveyAnswerId: Long,
    answer: AnswerWithPercentageChosen,
    clickOnAnswer: (AnswerWithPercentageChosen) -> Unit
) {
    val palette = LocalPaletteColors.current

    val isSelectedItem by remember(selectedSurveyAnswerId.value) {
        derivedStateOf { selectedSurveyAnswerId.value == answer.id }
    }

    val percentageAnswered = if (isYouAnswered){
        answer.percentageWhoChosen.collectAsState()
    } else {
        remember { mutableStateOf(Percentage(0f))  }
    }

    val supportModifier = remember(isYouAnswered) {
        if (isYouAnswered){
            if (rightSurveyAnswerId == answer.id){
                return@remember Modifier.background(palette.success)
            } else if (isSelectedItem) {
                return@remember Modifier.background(palette.error)
            }
        }
        Modifier
    }

    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = palette.backgroundPrimary,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .then(supportModifier)
            .changeBackgroundPerRipple(
                isDrawRippleBackground = isSelectedItem,
                isHandleClickable = !isYouAnswered,
                background = palette.contentPrimary,
                onClick = {
                    clickOnAnswer(answer)
                }
            )
            .padding(dimens.insidePadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = answer.text,
            fontSize = dimens.variationFontSize
        )
        if (isYouAnswered){
            Text(
                text = stringResource(R.string.percentage_pattern, (percentageAnswered.value.value * 100).toInt() ),
                fontSize = dimens.variationFontSize
            )
        }
    }
}