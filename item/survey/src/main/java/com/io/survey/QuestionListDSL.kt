package com.io.survey

fun List<QuestionWithSomeAnswers>.isAnsweredOnAllQuestion(): Boolean = all { it.isYouAnswered }

fun List<QuestionWithSomeAnswers>.nextIndex(): Int{
    val index = indexOfFirst { !it.isYouAnswered }
    return if (index == -1) lastIndex
    else index
}