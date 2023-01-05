package com.io.authorinteractionelements

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.io.survey.Percentage
import com.io.survey.SurveyAnswerModel
import com.io.survey.SurveyQuestionModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val surveyQuestions = listOf<SurveyQuestionModel>(
        SurveyQuestionModel(
            id = 0,
            question = "Первый вопрос",
            rightAnswer = SurveyAnswerModel(
                id = 1,
                percentageWhoChose = MutableStateFlow(Percentage(1f)),
                variation = "Один"
            ),
            isAnswered = false,
            variations = listOf(
                SurveyAnswerModel(
                    id = 1,
                    percentageWhoChose = MutableStateFlow(Percentage(1f)),
                    variation = "Один"
                ),
                SurveyAnswerModel(
                    id = 2,
                    percentageWhoChose = MutableStateFlow(Percentage(0f)),
                    variation = "Два"
                )
            ),
        ),
    ).toMutableStateList()
}