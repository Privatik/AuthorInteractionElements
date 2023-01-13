package com.io.authorinteractionelements.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.io.core.ui.ProjectTheme

@Composable
fun DefaultCard(
    content: @Composable () -> Unit
){
    val palette = ProjectTheme.palette
    val dimens = ProjectTheme.dimens
    val border = remember { BorderStroke(dimens.defaultBorder, palette.backgroundPrimary) }

    Card(
        modifier = Modifier
            .padding(dimens.cardOutPadding),
        backgroundColor = palette.backgroundTertiary,
        shape = MaterialTheme.shapes.medium,
        border = border,
        elevation = dimens.defaultElevation,
        content = content
    )
}