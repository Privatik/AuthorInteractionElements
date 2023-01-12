package com.io.core.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

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

    val variationFontSize: TextUnit
    val justVariationFontSize: TextUnit
    val questionFontSize: TextUnit

}