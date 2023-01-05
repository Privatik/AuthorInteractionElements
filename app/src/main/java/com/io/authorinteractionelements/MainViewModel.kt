package com.io.authorinteractionelements

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.io.survey.*
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val surveyQuestions = mockSurveys.toMutableStateList()

    fun updateSurveyQuestion(
        questionWithSomeTypeAnswers: QuestionWithSomeTypeAnswers,
        answerWithPercentageChosen: AnswerWithPercentageChosen
    ) {
        val index = surveyQuestions.indexOfFirst { questionWithSomeTypeAnswers.id == it.id  }
        surveyQuestions[index] = questionWithSomeTypeAnswers.copy(
            youAnswerId = answerWithPercentageChosen.id,
        )
    }
}