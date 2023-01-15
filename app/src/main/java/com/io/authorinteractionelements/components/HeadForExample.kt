package com.io.authorinteractionelements.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.io.core.ui.ProjectTheme

@Composable
fun <T: Any> HeadForExample(
    modifier: Modifier,
    items: List<T>,
    item: @Composable (index: Int, item: T) -> Unit
){
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(
            items = items,
            key = { index, _ -> index }
        ) { index, item ->
            item(index, item)

            if (index != items.lastIndex){
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = ProjectTheme.palette.divider
                )
            }
        }
    }
}