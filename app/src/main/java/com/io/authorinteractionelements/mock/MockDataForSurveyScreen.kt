package com.io.authorinteractionelements.mock

import androidx.compose.runtime.toMutableStateList
import com.io.survey.AnswerWithPercentageChosen
import com.io.survey.Percentage
import com.io.survey.QuestionWithSomeAnswers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

private val random: Random by lazy { Random(System.currentTimeMillis()) }
private val maxCountQuestion = 5

val mockDataForSurveysScreen = List(countItemsForMockScreen){
    List(random.nextInt(1, maxCountQuestion)) { questionIndex ->
        val countVarious = random.nextInt(2,mockQuestionToAnswer.size)
        val questionAndAnswer = mockQuestionToAnswer[random.nextInt(mockQuestionToAnswer.size)]

        var percentage = 1f
        val usedAnswers = hashSetOf<String>(

        )

        val various = mutableListOf<AnswerWithPercentageChosen>()

        AnswerWithPercentageChosen(
            id = 0,
            percentageWhoChosen = MutableStateFlow(Percentage(percentage - (random.nextFloat() * percentage))),
            text = questionAndAnswer.second,
        ).apply {
            usedAnswers += questionAndAnswer.second
            percentage -= percentageWhoChosen.value.value
            various.add(this)
        }

        repeat(countVarious - 1){ index ->
            var nextQuestionAnswer = questionAndAnswer.second
            while (usedAnswers.contains(nextQuestionAnswer)){
                nextQuestionAnswer = mockQuestionToAnswer[random.nextInt(mockQuestionToAnswer.size)].second
            }

            val currentPercentage = if (index == countVarious - 2){
                percentage
            } else {
                percentage - (random.nextFloat() * percentage)
            }

            AnswerWithPercentageChosen(
                id = (index + 1).toLong(),
                percentageWhoChosen = MutableStateFlow(Percentage(currentPercentage)),
                text = nextQuestionAnswer,
            ).apply {
                usedAnswers += nextQuestionAnswer
                percentage -= percentageWhoChosen.value.value
                various.add(this)
            }
        }

        QuestionWithSomeAnswers(
            id = questionIndex.toLong(),
            text = "Как пишется ${questionAndAnswer.first}?",
            rightAnswerId = 0,
            variationsAnswers = various.shuffled(),
        )
    }.toMutableStateList()
}