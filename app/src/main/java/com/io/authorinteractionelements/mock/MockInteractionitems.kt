package com.io.authorinteractionelements.mock

import com.io.interaction_text_compose.BodyForInteractionText

val mockInteractions = listOf(
    BodyForInteractionText(
        text = "1 пишется как \"###|textField|Один|###\", " +
                "а 2 пишется как \"###|textField|Два|###\", " +
                "а 3 пишется как \"###|textField|Три|###\".",
        pattern =  """###\|\w+\|\w+\|###""",
        indexAnsweredBlocks = hashSetOf()
    )
)