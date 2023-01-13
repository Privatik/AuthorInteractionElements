package com.io.authorinteractionelements

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.io.authorinteractionelements.mock.mockDataForSurveysScreen
import com.io.authorinteractionelements.mock.mockInteractions
import com.io.authorinteractionelements.mock.mockMatchItems
import com.io.authorinteractionelements.mock.randomOrdered
import com.io.survey.*

class MainViewModel: ViewModel() {

    val surveyTasks = mockDataForSurveysScreen.toMutableStateList()

    private val orderedColumnsMatchItems = mockMatchItems.randomOrdered()
    val firstOrderedColumn = orderedColumnsMatchItems.first.toMutableStateList()
    val secondOrderedColumn = orderedColumnsMatchItems.second.toMutableStateList()

    val interactionItemsForEdit = mockInteractions.toMutableStateList()
    val interactionItemsForDrag = mockInteractions
        .map { body ->
            body.copy(
                text = body.text.replace("textField", "dragBlock")
            )
        }
        .toMutableStateList()

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

    fun addIndexAsAnsweredInDragTask(
        index: Int,
        indexPattern: Int,
    ) {
        val body = interactionItemsForDrag[0]

        interactionItemsForDrag[0] = body.copy(
            indexAnsweredBlocks = (body.indexAnsweredBlocks + indexPattern)
        )
    }

    fun addIndexAsAnsweredInEditTask(
        index: Int,
        indexPattern: Int
    ) {
        val body = interactionItemsForEdit[0]

        interactionItemsForEdit[0] = body.copy(
            indexAnsweredBlocks = (body.indexAnsweredBlocks + indexPattern)
        )
    }


    fun foundMatchItems(
        index: Int,
        itemId: Long,
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