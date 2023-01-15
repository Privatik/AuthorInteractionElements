package com.io.authorinteractionelements.mock

import com.io.interaction_text_compose.BodyForInteractionText
import kotlin.random.Random

private val random: Random by lazy { Random(System.currentTimeMillis()) }

val mockInteractions = List(countItemsForMockScreen){
    val randomSize = random.nextInt(1, mockQuestionToAnswer.size)
    val builder = StringBuilder()

    (0 until randomSize).forEach { index ->
        val questionToAnswer = mockQuestionToAnswer[index]
        builder.append("${questionToAnswer.first} пишется как \"###|textField|${questionToAnswer.second}|###\"")
        if (index == randomSize - 1){
            builder.append(".")
        } else {
            builder.append(", ")
        }
    }

    BodyForInteractionText(
        text = builder.toString(),
        pattern =  """###\|\w+\|\w+\|###""",
        indexAnsweredBlocks = hashSetOf()
    )
}