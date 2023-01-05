package com.io.authorinteractionelements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.io.survey.*

@Composable
fun SurveyExampleScreen(
    modifier: Modifier = Modifier,
    questions: List<QuestionWithSomeTypeAnswers>,
    answerOnQuestion: (QuestionWithSomeTypeAnswers, AnswerWithPercentageChosen) -> Unit
){
    Column(
        modifier = modifier
    ) {
        MultiStageSurveys(
            modifier = Modifier.fillMaxWidth(),
            questions = questions,
            answerOnQuestion = { question, answer ->
                answerOnQuestion(question, answer)
            }
        )
    }

}