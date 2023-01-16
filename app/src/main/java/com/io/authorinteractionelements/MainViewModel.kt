package com.io.authorinteractionelements

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.io.authorinteractionelements.mock.*
import com.io.authorinteractionelements.screens.PairOrderedColumn
import com.io.stars.EvaluateInStars
import com.io.survey.*

class MainViewModel: ViewModel() {

    val surveyTasks = mockDataForSurveysScreen

    val orderedColumnsMatchItems = mockMatchItems
        .map { it.randomOrdered() }
        .map {
            PairOrderedColumn(
                orderedFirstColumn = it.first.toMutableStateList(),
                orderedSecondColumn = it.second.toMutableStateList()
            )
        }

    val interactionItemsForEdit = mockInteractions.toMutableStateList()
    val interactionItemsForDrag = mockInteractions
        .map { body ->
            body.copy(
                text = body.text.replace("textField", "dragBlock")
            )
        }
        .toMutableStateList()

    val evaluateInStarsList = List(countItemsForMockScreen){ EvaluateInStars() }
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
        val body = interactionItemsForDrag[index]

        interactionItemsForDrag[index] = body.copy(
            indexAnsweredBlocks = (body.indexAnsweredBlocks + indexPattern)
        )
    }

    fun addIndexAsAnsweredInEditTask(
        index: Int,
        indexPattern: Int
    ) {
        val body = interactionItemsForEdit[index]

        interactionItemsForEdit[index] = body.copy(
            indexAnsweredBlocks = (body.indexAnsweredBlocks + indexPattern)
        )
    }


    fun foundMatchItems(
        index: Int,
        itemId: Long,
    ) {
        val pairOrderedColumn = orderedColumnsMatchItems[index]

        val firstPairForMatchIndex =
            pairOrderedColumn.orderedFirstColumn.indexOfFirst { it.itemFromFirstColumn.id == itemId }
        val secondPairForMatchIndex =
            pairOrderedColumn.orderedSecondColumn.indexOfFirst { it.itemFromFirstColumn.id == itemId }
        val pairForMatch = pairOrderedColumn.orderedFirstColumn[firstPairForMatchIndex].copy(
            isFound = true
        )

        val nextIndex = pairOrderedColumn.orderedFirstColumn.count { it.isFound }

        (pairOrderedColumn.orderedFirstColumn as MutableList).apply {
            removeAt(firstPairForMatchIndex)
            pairOrderedColumn.orderedFirstColumn.add(nextIndex, pairForMatch)
        }

        (pairOrderedColumn.orderedSecondColumn as MutableList).apply {
            removeAt(secondPairForMatchIndex)
            add(nextIndex, pairForMatch)
        }

    }

    fun evaluateInStars(
        index: Int,
        countSelectedStars: Int,
    ) {
        val body = evaluateInStarsList[index]

        evaluateInStarsList[index] = body.copy(
            selectedStars = countSelectedStars
        )
    }
}