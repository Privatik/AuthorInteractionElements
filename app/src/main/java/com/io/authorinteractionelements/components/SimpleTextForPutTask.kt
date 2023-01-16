package com.io.authorinteractionelements.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme


@Composable
fun SimpleTextForPutTask(
    text: String
){
    Text(
        modifier = Modifier
            .height(ProjectTheme.dimens.defaultBlockHeight)
            .padding(horizontal = 2.dp),
        text = text,
        fontSize = ProjectTheme.dimens.justVariationFontSize,
        color = ProjectTheme.palette.textSecondary
    )
}