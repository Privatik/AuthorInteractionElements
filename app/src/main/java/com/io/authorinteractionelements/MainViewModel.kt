package com.io.authorinteractionelements

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.io.authorinteractionelements.mock.mockDataForSurveysScreen
import com.io.authorinteractionelements.mock.mockMatchItems
import com.io.authorinteractionelements.mock.randomOrdered
import com.io.survey.*

class MainViewModel: ViewModel() {

    val surveyTasks = mockDataForSurveysScreen.toMutableStateList()

    private val orderedColumnsMatchItems = mockMatchItems.randomOrdered()
    val firstOrderedColumn = orderedColumnsMatchItems.first.toMutableStateList()
    val secondOrderedColumn = orderedColumnsMatchItems.second.toMutableStateList()

    fun updateSurveyQuestion(
        taskIndex: Int,
        questionWithSomeAnswers: QuestionWithSomeAnswers,
        answerWithPercentageChosen: AnswerWithPercentageChosen
    ) {
        val currentQuestions = surveyTasks[taskIndex]
        val index = currentQuestions.indexOfFirst { questionWithSomeAnswers.id == it.id  }
        currentQuestions[index] = questionWithSomeAnswers.copy(
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