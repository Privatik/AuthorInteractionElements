package com.io.put_skip_per_drag

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DragItem(
    modifier: Modifier = Modifier,
    item: InteractionElement
){

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = item.value as String)
    }
}