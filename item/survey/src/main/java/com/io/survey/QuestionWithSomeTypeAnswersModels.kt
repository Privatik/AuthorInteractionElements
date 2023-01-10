package com.io.survey

import androidx.annotation.FloatRange
import kotlinx.coroutines.flow.StateFlow

data class QuestionWithSomeAnswers(
    val id: Long,
    val text: String,
    val rightAnswerId: Long,
    val variationsAnswers: List<AnswerWithPercentageChosen>,
    val youAnswerId: Long? = null,
) {
    val isYouAnswered: Boolean
        get() = youAnswerId != null
}

data class AnswerWithPercentageChosen(
    val id: Long,
    val percentageWhoChosen: StateFlow<Percentage>,
    val text: String
)

data class Percentage(
    @FloatRange(from = 0.0, to = 1.0)
    val value: Float
)
