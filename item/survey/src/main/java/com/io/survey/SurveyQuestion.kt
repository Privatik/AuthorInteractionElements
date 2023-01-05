package com.io.survey

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens

@Composable
fun SurveyQuestion(
    modifier: Modifier = Modifier,
    questionWithAnswers: QuestionWithSomeTypeAnswers,
    answerOnQuestion: (QuestionWithSomeTypeAnswers, AnswerWithPercentageChosen) -> Unit
) {
    val selectedSurveyAnswer = rememberSaveable { mutableStateOf<Long?>(questionWithAnswers.youAnswerId) }
    val isSelectedAlmostOneItem by remember(questionWithAnswers.isYouAnswered) {
        derivedStateOf { !questionWithAnswers.isYouAnswered && selectedSurveyAnswer.value != null }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = questionWithAnswers.text,
            fontSize = dimens.questionFontSize,
        )
        Spacer(modifier = Modifier.height(dimens.smallSpace))
        LazyColumn(
            modifier = Modifier
                .height(dimens.maxBlockSize)
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
        if (isSelectedAlmostOneItem){
           BottomSelectButton(
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
    questionWithAnswers: QuestionWithSomeTypeAnswers,
    answerOnQuestion: (QuestionWithSomeTypeAnswers, AnswerWithPercentageChosen) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(dimens.smallSpace))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ProjectTheme.palette.backgroundPrimary
            ),
            onClick = {
                answerOnQuestion(
                    questionWithAnswers,
                    questionWithAnswers.variationsAnswers.first { it.id == selectedSurveyAnswer.value }
                )
            }
        ) {
            Text(
                text = stringResource(R.string.choice),
                color = ProjectTheme.palette.contentPrimary
            )
        }
    }
}