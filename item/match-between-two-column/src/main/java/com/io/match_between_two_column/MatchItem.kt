package com.io.match_between_two_column

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.io.core.ui.LocalPaletteColors
import com.io.core.ui.ProjectTheme
import com.io.item.rippleBackground

@Composable
fun MatchItem(
    modifier: Modifier = Modifier,
    selectedItemId: State<Long?>,
    item: ItemColumn,
    isFound: Boolean,
    selectItem: (ItemColumn) -> Unit,
){
    val palette = LocalPaletteColors.current
    val supportModifier = remember(isFound) {
        if (isFound){
            return@remember Modifier.background(palette.success)
        }
        Modifier
    }

    val isSelectedItem = remember {
        derivedStateOf { selectedItemId.value == item.id }
    }

    Box(
        modifier = modifier
            .then(supportModifier)
            .rippleBackground(
                isHandleClickable = !isFound,
                isDrawRippleBackground = isSelectedItem.value,
                color = palette.contentPrimary,
                onClick = {
                    selectItem(item)
                },
            )
            .padding(ProjectTheme.dimens.insidePadding)
    ){
        Text(text = item.text)
    }
}