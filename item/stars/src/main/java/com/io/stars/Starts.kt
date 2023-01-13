@file:OptIn(ExperimentalTextApi::class)

package com.io.stars

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette
import kotlinx.coroutines.launch

@Composable
fun Stars(
    modifier: Modifier = Modifier,
    @androidx.annotation.IntRange(from = 1) countStars: Int = 5,
    sizeOneStar: Dp = 50.dp,
    paddingBetweenStar: Dp = 10.dp,
    evaluateInStars: EvaluateInStars,
    doEvaluate: (selectedStar: Int) -> Unit
){
    val density = LocalDensity.current

    val path = remember { Path() }

    val scope = rememberCoroutineScope()

    val widthBlock = remember {
        (sizeOneStar.times(countStars)) + paddingBetweenStar.times(countStars - 1)
    }

    val sizeOneStarPx = remember(density) {
        with(density){ sizeOneStar.roundToPx() }.toFloat().let { Size(it,it) }
    }

    val paddingBetweenStarPx = remember(density) {
        with(density){ paddingBetweenStar.roundToPx() }.toFloat()
    }

    val offsetStars = remember {
        starsOffsets(
            countStars = countStars,
            sizeOneStar = sizeOneStarPx,
            paddingBetweenStar = paddingBetweenStarPx,
        )
    }

    val selectedStarts = remember { mutableStateOf(evaluateInStars.selectedStars) }

    val doAnimate = remember { mutableStateOf(false) }
    val isShowResult = remember { mutableStateOf(evaluateInStars.selectedStars != 0) }
    val isShowFinalSelectedStars = remember { mutableStateOf(evaluateInStars.selectedStars != 0) }
    val canHandleClicksByStars = remember { mutableStateOf(evaluateInStars.selectedStars == 0) }
    val isFirstStarInCenter = remember { mutableStateOf(evaluateInStars.selectedStars != 0) }

    val enabled = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
           modifier = Modifier
               .width(widthBlock)
               .height(sizeOneStar)
               .starsMovement(
                   isStartAnimation = doAnimate,
                   isShowResult = isShowResult,
                   animableOffsetsStars = offsetStars,
                   sizeOneStarPx = sizeOneStarPx,
                   finishJumpAnimation = {
                       isFirstStarInCenter.value = true
                       canHandleClicksByStars.value = false
                   },
                   finishMoveToCenterAnimation = {
                       isShowFinalSelectedStars.value = true
                   }
               )
               .drawStarts(
                   starsPath = path,
                   isDrawOnlyFirstStar = isFirstStarInCenter,
                   animableOffsetsStars = offsetStars,
                   sizeOneStar = sizeOneStarPx
               )
               .drawStarsProgressLineByPath(
                   starsPath = path,
                   canHandleClicks = canHandleClicksByStars,
                   animableOffsetsStars = offsetStars,
                   sizeOneStarPx = sizeOneStarPx,
                   selectedStars = { countSelectedStars ->
                       selectedStarts.value = countSelectedStars
                       enabled.value = true
                   },
               )
               .drawCountSelectedStarts(
                   isDrawTextState = isShowFinalSelectedStars,
                   selectedStartsState = selectedStarts,
                   offsetAnimableState = offsetStars[0],
                   starOneSize = sizeOneStarPx
               ),
        )

        Spacer(modifier = Modifier.height(dimens.smallSpace))

        if (!isShowResult.value){
            EvaluateButton(
                enabled = enabled.value
            ) {
                doAnimate.value = true
                isShowResult.value = true
                scope.launch {
                    doEvaluate(selectedStarts.value)
                }
            }
        }
    }
}

@Composable
private fun EvaluateButton(
    enabled: Boolean,
    onClick: () -> Unit
){
    val backgroundButtonColor by animateColorAsState(
        targetValue = if (enabled) palette.backgroundSecondary else palette.backgroundPrimary
    )
    val textColor by animateColorAsState(
        targetValue = if (enabled) palette.textSecondary else palette.textTertiary
    )

    Button(
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundButtonColor,
            disabledBackgroundColor = backgroundButtonColor
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text = stringResource(R.string.evaluate),
            color = textColor
        )
    }
}