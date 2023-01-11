package com.io.authorinteractionelements.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
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
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(tasks) { index, questions ->
            MultiStageSurveys(
                modifier = Modifier.fillMaxWidth(),
                questions = questions,
                answerOnQuestion = { question, answer ->
                    answerOnQuestion(index, question, answer)
                }
            )
            if (index != tasks.lastIndex){
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = palette.divider
                )
            }
        }
    }

}