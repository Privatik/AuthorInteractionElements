package com.io.survey

fun List<QuestionWithSomeTypeAnswers>.isAnsweredOnAllQuestion(): Boolean = all { it.isYouAnswered }

fun List<QuestionWithSomeTypeAnswers>.nextIndex(): Int{
    val index = indexOfFirst { !it.isYouAnswered }
    return if (index == -1) lastIndex
    else index
}