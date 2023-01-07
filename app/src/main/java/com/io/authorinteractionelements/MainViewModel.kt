package com.io.authorinteractionelements

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.io.survey.*

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

    fun foundMatchItems(
        itemId: Long
    ) {
        val firstPairForMatchIndex =
            firstOrderedColumn.indexOfFirst { it.itemFromFirstColumn.id == itemId }
        val secondPairForMatchIndex =
            secondOrderedColumn.indexOfFirst { it.itemFromFirstColumn.id == itemId }
        val pairForMatch = firstOrderedColumn[firstPairForMatchIndex].copy(
            isFound = true
        )

        val nextIndex = firstOrderedColumn.count { it.isFound }

        firstOrderedColumn.removeAt(firstPairForMatchIndex)
        firstOrderedColumn.add(nextIndex, pairForMatch)

        secondOrderedColumn.removeAt(secondPairForMatchIndex)
        secondOrderedColumn.add(nextIndex, pairForMatch)
    }
}