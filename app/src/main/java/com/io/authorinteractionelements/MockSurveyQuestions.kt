package com.io.authorinteractionelements

import com.io.survey.AnswerWithPercentageChosen
import com.io.survey.Percentage
import com.io.survey.QuestionWithSomeTypeAnswers
import kotlinx.coroutines.flow.MutableStateFlow

val mockSurveys = listOf<QuestionWithSomeTypeAnswers>(
    QuestionWithSomeTypeAnswers(
        id = 0,
        text = "Как пишется 1?",
        rightAnswerId = 1,
        variationsAnswers = listOf(
            AnswerWithPercentageChosen(
                id = 1,
                percentageWhoChosen = MutableStateFlow(Percentage(1f)),
                text = "Один"
            ),
            AnswerWithPercentageChosen(
                id = 2,
                percentageWhoChosen = MutableStateFlow(Percentage(0f)),
                text = "Два"
            )
        ),
    ),
)