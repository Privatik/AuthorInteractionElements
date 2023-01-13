package com.io.survey

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
import com.io.item.rippleBackground
import com.io.item.scaleSelectAnswer

@Composable
internal fun SurveyAnswer(
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

    Row(
        modifier = modifier
            .scaleSelectAnswer(isSelectedItem)
            .border(
                width = dimens.defaultBorder,
                color = palette.backgroundPrimary,
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .rippleBackground(
                isDrawRippleBackground = isSelectedItem,
                isHandleClickable = !isYouAnswered,
                color = palette.buttonRippleOnContent,
                onClick = {
                    clickOnAnswer(answer)
                }
            )
            .rippleBackground(
                isDrawRippleBackground = isYouAnswered && (rightSurveyAnswerId == answer.id || isSelectedItem),
                color = if (rightSurveyAnswerId == answer.id) palette.success else palette.error
            )
            .padding(dimens.insidePadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = answer.text,
            fontSize = dimens.variationFontSize,
            color = palette.textSecondary,
        )
        if (isYouAnswered){
            Text(
                text = stringResource(R.string.percentage_pattern, percentageAnswered.percentage().toInt() ),
                fontSize = dimens.variationFontSize,
                color = palette.textSecondary,
            )
        }
    }
}

private fun State<Percentage>.percentage(): Float{
    return value.value * 100
}