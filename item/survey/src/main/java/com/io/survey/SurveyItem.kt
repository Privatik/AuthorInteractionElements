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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.Theme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SurveyItem(
    modifier: Modifier = Modifier,
    surveyQuestionModel: SurveyQuestionModel,
    selectAnswer: (SurveyQuestionModel, SurveyAnswerModel) -> Unit
) {
    val selectedSurveyAnswer = rememberSaveable {
        mutableStateOf<Long?>(surveyQuestionModel.youAnswerId)
    }

    val isSelectedAlmostOneItem by remember(surveyQuestionModel.isAnswered) {
        derivedStateOf { !surveyQuestionModel.isAnswered && selectedSurveyAnswer.value != null }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = surveyQuestionModel.question,
            fontSize = dimens.questionFontSize,
        )
        Spacer(modifier = Modifier.height(dimens.smallSpace))
        LazyColumn(
            modifier = Modifier
                .heightIn(min = 0.dp, dimens.maxBlockSize)
        ) {
            itemsIndexed(surveyQuestionModel.variations){ index, answer: SurveyAnswerModel ->
                SurveyAnswer(
                    modifier = Modifier.fillMaxWidth(),
                    surveyAnswer = answer,
                    isAnswered = surveyQuestionModel.isAnswered,
                    rightSurveyAnswerModel = surveyQuestionModel.rightAnswer,
                    selectedSurveyAnswerId = selectedSurveyAnswer,
                    clickOnAnswer = {
                        selectedSurveyAnswer.value = it.id
                    },
                )
                if (index != surveyQuestionModel.variations.lastIndex){
                    Spacer(modifier = Modifier.height(dimens.smallSpace))
                }
            }
        }
        if (isSelectedAlmostOneItem){
            Spacer(modifier = Modifier.height(dimens.smallSpace))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ProjectTheme.palette.backgroundPrimary
                ),
                onClick = {
                    selectAnswer(
                        surveyQuestionModel,
                        surveyQuestionModel.variations.first { it.id == selectedSurveyAnswer.value }
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
}

@Preview
@Composable
fun TestSurveyItem(){
    val firstAnswer = SurveyAnswerModel(
        id = 1,
        percentageWhoChose = MutableStateFlow(Percentage(1f)),
        variation = "Один"
    )
    val secondAnswer = SurveyAnswerModel(
        id = 2,
        percentageWhoChose = MutableStateFlow(Percentage(0f)),
        variation = "Два"
    )

    Theme {
        SurveyItem(
            surveyQuestionModel = SurveyQuestionModel(
                id = 0,
                question = "Первый вопрос",
                isAnswered = false,
                rightAnswer = firstAnswer,
                variations = listOf(firstAnswer, secondAnswer),
            ),
            selectAnswer = { surveyQuestionModel, surveyAnswerModel ->

            }
        )
    }
}

@Preview
@Composable
fun TestAnsweredSurveyItem(){
    val firstAnswer = SurveyAnswerModel(
        id = 1,
        percentageWhoChose = MutableStateFlow(Percentage(1f)),
        variation = "Один"
    )
    val secondAnswer = SurveyAnswerModel(
        id = 2,
        percentageWhoChose = MutableStateFlow(Percentage(0f)),
        variation = "Два"
    )

    Theme {
        SurveyItem(
            surveyQuestionModel = SurveyQuestionModel(
                id = 0,
                question = "Первый вопрос",
                isAnswered = true,
                rightAnswer = firstAnswer,
                variations = listOf(firstAnswer, secondAnswer),
            ),
            selectAnswer = { surveyQuestionModel, surveyAnswerModel ->

            }
        )
    }
}