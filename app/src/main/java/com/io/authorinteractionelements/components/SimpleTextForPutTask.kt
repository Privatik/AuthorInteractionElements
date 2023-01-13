package com.io.authorinteractionelements.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.io.core.ui.ProjectTheme


@Composable
fun SimpleTextForPutTask(
    text: String
){
    Text(
        modifier = Modifier.height(ProjectTheme.dimens.defaultBlockHeight),
        text = text,
        fontSize = ProjectTheme.dimens.justVariationFontSize,
        color = ProjectTheme.palette.textSecondary
    )
}