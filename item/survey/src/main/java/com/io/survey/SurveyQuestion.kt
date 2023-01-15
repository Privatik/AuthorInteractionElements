package com.io.survey

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.io.core.ui.DefaultDimens.heightQuestionBlock
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette

@Composable
internal fun SurveyQuestion(
    modifier: Modifier = Modifier,
    questionWithAnswers: QuestionWithSomeAnswers,
    answerOnQuestion: (QuestionWithSomeAnswers, AnswerWithPercentageChosen) -> Unit
) {
    val selectedSurveyAnswer = rememberSaveable { mutableStateOf<Long?>(questionWithAnswers.youAnswerId) }
    val isSelectedAtLeastOneItem = remember(questionWithAnswers.isYouAnswered) {
        derivedStateOf { !questionWithAnswers.isYouAnswered && selectedSurveyAnswer.value != null }
    }
    val scrollState = rememberLazyListState()

    Column(
        modifier = modifier.height(heightQuestionBlock)
    ) {
        Text(
            text = questionWithAnswers.text,
            fontSize = dimens.questionFontSize,
            color = palette.textSecondary,
        )
        Spacer(modifier = Modifier.height(dimens.smallSpace))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .verticalScrollbar(
                    state = scrollState,
                    color = palette.backgroundPrimary
                ),
            state = scrollState,
            contentPadding = PaddingValues(dimens.insidePadding)
        ) {
            itemsIndexed(questionWithAnswers.variationsAnswers){ index, answer ->
                SurveyAnswer(
                    modifier = Modifier.fillMaxWidth(),
                    answer = answer,
                    isYouAnswered = questionWithAnswers.isYouAnswered,
                    rightSurveyAnswerId = questionWithAnswers.rightAnswerId,
                    selectedSurveyAnswerId = selectedSurveyAnswer,
                    clickOnAnswer = {
                        selectedSurveyAnswer.value = it.id
                    },
                )
                if (index != questionWithAnswers.variationsAnswers.lastIndex){
                    Spacer(modifier = Modifier.height(dimens.smallSpace))
                }
            }
        }

        AnimatedVisibility(
            visible = !questionWithAnswers.isYouAnswered
        ) {
            BottomSelectButton(
                enabled = isSelectedAtLeastOneItem,
                selectedSurveyAnswer = selectedSurveyAnswer,
                questionWithAnswers = questionWithAnswers,
                answerOnQuestion = answerOnQuestion,
            )
        }
    }
}

@Composable
private fun BottomSelectButton(
    selectedSurveyAnswer: State<Long?>,
    enabled: State<Boolean>,
    questionWithAnswers: QuestionWithSomeAnswers,
    answerOnQuestion: (QuestionWithSomeAnswers, AnswerWithPercentageChosen) -> Unit
){
    val palette = palette
    val backgroundButtonColor by animateColorAsState(
        targetValue = if (enabled.value) palette.backgroundSecondary else palette.backgroundPrimary
    )
    val textButtonColor by animateColorAsState(
        targetValue = if (enabled.value) palette.textSecondary else palette.textTertiary
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.smallSpace + heightButtonBlock),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(dimens.smallSpace))
        Button(
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundButtonColor,
                disabledBackgroundColor = backgroundButtonColor
            ),
            enabled = enabled.value,
            onClick = {
                answerOnQuestion(
                    questionWithAnswers,
                    questionWithAnswers.variationsAnswers.first { it.id == selectedSurveyAnswer.value }
                )
            }
        ) {
            Text(
                text = stringResource(R.string.choice),
                color = textButtonColor
            )
        }
    }
}

private val heightButtonBlock = 70.dp