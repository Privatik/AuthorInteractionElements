package com.io.authorinteractionelements.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.io.authorinteractionelements.components.HeadForExample
import com.io.survey.*

@Composable
fun SurveyExampleScreen(
    modifier: Modifier = Modifier,
    tasks: List<List<QuestionWithSomeAnswers>>,
    answerOnQuestion: (
        taskIndex: Int,
        questionWithSomeAnswers: QuestionWithSomeAnswers,
        answerWithPercentageChosen: AnswerWithPercentageChosen,
    ) -> Unit
){
    HeadForExample(
        modifier = modifier,
        items = tasks
    ) { index, questions ->
        MultiStageSurveys(
            modifier = Modifier.fillMaxWidth(),
            questions = questions,
            answerOnQuestion = { question, answer ->
                answerOnQuestion(index, question, answer)
            }
        )
    }
}