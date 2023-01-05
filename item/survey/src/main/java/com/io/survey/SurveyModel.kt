package com.io.survey

import androidx.annotation.FloatRange
import kotlinx.coroutines.flow.StateFlow

data class SurveyModel(
    val questions: List<SurveyQuestionModel>
)

data class SurveyQuestionModel(
    val id: Long,
    val question: String,
    val isAnswered: Boolean,
    val rightAnswer: SurveyAnswerModel,
    val variations: List<SurveyAnswerModel>,
    val youAnswerId: Long? = null,
)

data class SurveyAnswerModel(
    // на случай отслеживаний изменений в пеальном времени
    val id: Long,
    val percentageWhoChose: StateFlow<Percentage>,
    val variation: String
)

data class Percentage(
    @FloatRange(from = 0.0, to = 1.0)
    val value: Float
)
