package com.io.core.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.io.core.ui.Dimens

object DefaultDimens: Dimens {
    override val smallSpace: Dp = 10.dp
    override val mediumSpace: Dp = 20.dp
    override val maxBlockSize: Dp = 150.dp
    override val insidePadding: Dp = 7.dp
    override val horizontalPadding: Dp = 16.dp
    override val variationFontSize: TextUnit = 12.sp
    override val questionFontSize: TextUnit = 20.sp

}