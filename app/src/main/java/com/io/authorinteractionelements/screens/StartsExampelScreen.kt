package com.io.authorinteractionelements.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.io.authorinteractionelements.components.DefaultCard
import com.io.authorinteractionelements.components.HeadForExample
import com.io.stars.EvaluateInStars
import com.io.stars.Stars

@Composable
fun StarsExampleScreen(
    modifier: Modifier = Modifier,
    evaluateInStarsList: List<EvaluateInStars>,
    doEvaluate: (index: Int, countSelectedStars: Int) -> Unit
){

    HeadForExample(
        modifier = modifier,
        items = evaluateInStarsList,
    ) { index, evaluateInStars ->
        DefaultCard {
            Stars(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                evaluateInStars = evaluateInStars,
                doEvaluate = { countSelectedStars ->
                    doEvaluate(index, countSelectedStars)
                }
            )
        }
    }
}