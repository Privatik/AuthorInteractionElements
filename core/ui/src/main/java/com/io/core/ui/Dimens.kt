package com.io.core.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

interface Dimens {
    val smallSpace: Dp
    val mediumSpace: Dp

    val heightQuestionBlock: Dp

    val insidePadding: Dp
    val cardOutPadding: Dp

    val dividerSpace: Dp

    val horizontalPadding: Dp

    val defaultBlockHeight: Dp
    val defaultBlockWidth: Dp
    val defaultBorder: Dp
    val defaultElevation: Dp

    val variationFontSize: TextUnit
    val justVariationFontSize: TextUnit
    val questionFontSize: TextUnit

}