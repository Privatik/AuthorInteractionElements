package com.io.survey

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.io.core.ui.ProjectTheme.dimens
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun MultiStageSurveys(
    modifier: Modifier = Modifier,
    questions: List<QuestionWithSomeTypeAnswers>,
    answerOnQuestion: (
        QuestionWithSomeTypeAnswers,
        AnswerWithPercentageChosen,
    ) -> Unit
) {
    val scrollState = rememberLazyListState(questions.nextIndex())
    val scope = rememberCoroutineScope()
    val canScroll by remember(scrollState) { derivedStateOf { questions.isAnsweredOnAllQuestion() } }

    QuestionPager(
        scrollState = scrollState,
        userScrollEnabled = canScroll,
    ) {

    }
    var width by remember { mutableStateOf(0) }

    Canvas(
        modifier = Modifier.fillMaxWidth(),
    ) {
        width = size.width.roundToInt()
    }

    LaunchedEffect(Unit){
        scrollState.interactionSource
            .interactions
            .collect{
                when (it){
                    is DragInteraction.Cancel,
                    is DragInteraction.Stop -> {
                        scrollState.finishScroll(this, width)
                    }
                }
            }
    }

    LazyRow(
        modifier = modifier,
        state = scrollState,
        userScrollEnabled = canScroll,
    ) {
        itemsIndexed(questions) { page, item ->
            Column(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(dimens.insidePadding)
            ) {
                Text(
                    text = stringResource(
                        R.string.question_one_of_count,
                        page + 1,
                        questions.size
                    )
                )
                Spacer(modifier = Modifier.height(dimens.mediumSpace))

                SurveyQuestion(
                    questionWithAnswers = item,
                    answerOnQuestion = { surveyQuestionModel, surveyAnswerModel ->
                        answerOnQuestion(surveyQuestionModel, surveyAnswerModel)
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
}