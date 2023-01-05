package com.io.survey

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.io.core.ui.ProjectTheme.dimens

@Composable
fun SliderSurveys(
    modifier: Modifier = Modifier,
    surveyModel: SurveyModel,
    onAnsweredOnQuestion: (SurveyQuestionModel, SurveyAnswerModel) -> Unit
) {
    if (surveyModel.questions.isNotEmpty()){
        var nextIndex = 0
        while (surveyModel.questions[nextIndex++].isAnswered) {
            if (nextIndex < surveyModel.questions.size) break
        }

        val currentIndex = remember { mutableStateOf(nextIndex) }
        val currentQuestion by remember(currentIndex.value) {
            mutableStateOf(surveyModel.questions[nextIndex])
        }
        val count = surveyModel.questions.size

        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.question_one_of_count, currentIndex, count))
            Spacer(modifier = Modifier.height(dimens.mediumSpace))

            
            SurveyItem(
                surveyQuestionModel = currentQuestion,
                selectAnswer = onAnsweredOnQuestion
            )
        }
    }
}