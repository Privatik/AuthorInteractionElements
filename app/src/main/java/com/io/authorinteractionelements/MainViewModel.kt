package com.io.authorinteractionelements

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.io.survey.*
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {

    val surveyQuestions = mockSurveys.toMutableStateList()
    private val orderedColumnsMatchItems = mockMatchItems.randomOrdered()
    val firstOrderedColumn = orderedColumnsMatchItems.first.toMutableStateList()
    val secondOrderedColumn = orderedColumnsMatchItems.second.toMutableStateList()

    fun updateSurveyQuestion(
        questionWithSomeTypeAnswers: QuestionWithSomeTypeAnswers,
        answerWithPercentageChosen: AnswerWithPercentageChosen
    ) {
        val index = surveyQuestions.indexOfFirst { questionWithSomeTypeAnswers.id == it.id  }
        surveyQuestions[index] = questionWithSomeTypeAnswers.copy(
            youAnswerId = answerWithPercentageChosen.id,
        )
    }

    fun matchIds(
        firstItemId: Long?,
        secondItemId: Long?
    ) {
        if (firstItemId == secondItemId && secondItemId != null){
            val firstPairForMatchIndex = firstOrderedColumn.indexOfFirst { it.itemFromFirstColumn.id == firstItemId }
            val secondPairForMatchIndex = secondOrderedColumn.indexOfFirst { it.itemFromFirstColumn.id == secondItemId }
            val pairForMatch = firstOrderedColumn[firstPairForMatchIndex].copy(
                isFound = true
            )

            firstOrderedColumn[firstPairForMatchIndex] = pairForMatch
            secondOrderedColumn[secondPairForMatchIndex] = pairForMatch
        }
    }
}