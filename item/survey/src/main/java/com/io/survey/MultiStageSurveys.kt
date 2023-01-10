package com.io.survey

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import kotlinx.coroutines.launch

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
    val swipableState = rememberSwipableState(countItems = questions.size)

    SwipableBox(state = swipableState) { page ->
        val palette = palette
        val item by remember { derivedStateOf { questions[page] } }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.cardOutPadding),
            backgroundColor = palette.backgroundTertiary,
            shape = MaterialTheme.shapes.medium,
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
            }
        }
    }
//    QuestionPager(
//        modifier = modifier,
//        scrollState = scrollState,
//        userScrollEnabled = canScroll,
//    ) {
//        itemsIndexed(questions) { page, item ->
//            val palette = palette
////            val border = remember {
////                BorderStroke(1.dp, palette.backgroundSecondary)
////            }
//
//            Card(
//                modifier = Modifier
//                    .fillParentMaxWidth()
//                    .padding(dimens.cardOutPadding),
//                backgroundColor = palette.backgroundTertiary,
//                shape = MaterialTheme.shapes.medium,
////                border = border,
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
//                    Text(
//                        modifier = Modifier
//                            .padding(dimens.insidePadding),
//                        text = stringResource(
//                            R.string.question_one_of_count,
//                            page + 1,
//                            questions.size
//                        ),
//                        color = palette.textSecondary,
//                    )
//                    Spacer(modifier = Modifier.height(dimens.mediumSpace))
//
//                    SurveyQuestion(
//                        modifier = Modifier.padding(dimens.insidePadding),
//                        questionWithAnswers = item,
//                        answerOnQuestion = { question, answer ->
//                            answerOnQuestion(question, answer)
//                            scope.launch {
//                                if (page != questions.lastIndex){
//                                    scrollState.animateScrollToItem(page + 1)
//                                }
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
}