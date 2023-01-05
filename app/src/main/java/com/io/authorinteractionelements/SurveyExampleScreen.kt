package com.io.authorinteractionelements

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.io.survey.SurveyAnswerModel
import com.io.survey.SurveyItem
import com.io.survey.SurveyQuestionModel

@Composable
fun SurveyExampleScreen(
    modifier: Modifier = Modifier,
    questions: List<SurveyQuestionModel>,
    onAnsweredOnQuestion: (SurveyQuestionModel, SurveyAnswerModel) -> Unit
){
    LazyColumn(
        modifier = modifier
    ) {
        items(questions){ item ->
            SurveyItem(
                surveyQuestionModel = item,
                selectAnswer = onAnsweredOnQuestion
            )
        }
    }

}