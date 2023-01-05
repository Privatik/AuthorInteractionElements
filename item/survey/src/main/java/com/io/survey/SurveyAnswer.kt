package com.io.survey

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.io.core.ui.LocalPaletteColors
import com.io.core.ui.ProjectTheme.dimens
import kotlinx.coroutines.launch

@Composable
fun SurveyAnswer(
    modifier: Modifier = Modifier,
    isYouAnswered: Boolean,
    selectedSurveyAnswerId: State<Long?>,
    rightSurveyAnswerId: Long,
    answer: AnswerWithPercentageChosen,
    clickOnAnswer: (AnswerWithPercentageChosen) -> Unit
) {
    val palette = LocalPaletteColors.current

    val isSelectedItem by remember(selectedSurveyAnswerId.value) {
        derivedStateOf { selectedSurveyAnswerId.value == answer.id }
    }

    val percentageAnswered = if (isYouAnswered){
        answer.percentageWhoChosen.collectAsState()
    } else {
        remember { mutableStateOf(Percentage(0f))  }
    }

    var size by remember {
        mutableStateOf(Size.Unspecified)
    }

    val supportModifier = remember(isYouAnswered) {
        if (isYouAnswered){
            if (rightSurveyAnswerId == answer.id){
                return@remember Modifier.background(palette.success)
            } else if (isSelectedItem) {
                return@remember Modifier.background(palette.error)
            }
        }
        Modifier
    }

    var lastClickPosition by remember {
        mutableStateOf(Offset.Zero)
    }

    val radius = remember { Animatable(0f) }

    LaunchedEffect(isSelectedItem){
        if (isSelectedItem){
            launch {
                radius.animateTo(
                    targetValue = size.width,
                    animationSpec = tween(durationMillis = 400),
                )
            }
        } else {
            launch {
                radius.snapTo(0f)
            }
        }

    }

    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = palette.backgroundPrimary,
                shape = MaterialTheme.shapes.small
            )
            .pointerInput(isYouAnswered) {
                if (!isYouAnswered) {
                    detectTapGestures(
                        onTap = {
                            if (!isSelectedItem) {
                                lastClickPosition = it
                                clickOnAnswer(answer)
                            }
                        }
                    )
                }
            }
            .clip(MaterialTheme.shapes.small)
            .then(supportModifier)
            .drawBehind {
                if (!isYouAnswered) {
                    drawCircle(
                        color = palette.contentPrimary,
                        radius = radius.value,
                        center = lastClickPosition,
                    )
                }
            }
            .onSizeChanged {
                if (size.isUnspecified) {
                    size = Size(it.width.toFloat(), it.height.toFloat())
                }
            }
            .padding(dimens.insidePadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = answer.text,
            fontSize = dimens.variationFontSize
        )
        if (isYouAnswered){
            Text(
                text = stringResource(R.string.percentage_pattern, (percentageAnswered.value.value * 100).toInt() ),
                fontSize = dimens.variationFontSize
            )
        }
    }
}