package com.io.survey

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun MultiStageSurveys(
    modifier: Modifier = Modifier,
    questions: List<QuestionWithSomeAnswers>,
    answerOnQuestion: (
        QuestionWithSomeAnswers,
        AnswerWithPercentageChosen,
    ) -> Unit
) {
    val scrollState = rememberLazyListState(questions.nextIndex())
    val scope = rememberCoroutineScope()
    val canScroll by remember(scrollState) { derivedStateOf { questions.isAnsweredOnAllQuestion() } }
    val swipableState = rememberLeftSwipableState(countItems = questions.size)

    LeftSwipableBox(
        state = swipableState,
        userScrollEnabled = false,
    ) { page ->
        val palette = palette
        val item by remember { derivedStateOf { questions[page] } }
        val border = remember { BorderStroke(2.dp, palette.backgroundPrimary) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.cardOutPadding),
            backgroundColor = palette.backgroundTertiary,
            shape = MaterialTheme.shapes.medium,
            border = border,
            elevation = 3.dp
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    modifier = Modifier
                        .padding(dimens.insidePadding),
                    text = stringResource(
                        R.string.question_one_of_count,
                        page + 1,
                        questions.size
                    ),
                    color = palette.textSecondary,
                )
                Spacer(modifier = Modifier.height(dimens.mediumSpace))

                SurveyQuestion(
                    modifier = Modifier.padding(dimens.insidePadding),
                    questionWithAnswers = item,
                    answerOnQuestion = { question, answer ->
                        answerOnQuestion(question, answer)
                        scope.launch {
                            if (page != questions.lastIndex){
                                scrollState.animateScrollToItem(page + 1)
                            }
                        }
                    }
                )
                Button(onClick = {
                    scope.launch {
                        swipableState.animateScrollTo(Random(System.currentTimeMillis()).nextInt(questions.size).also {
                            println("scroll To $it")
                        })
                    }
                }) {

                }
            }
        }
    }
}