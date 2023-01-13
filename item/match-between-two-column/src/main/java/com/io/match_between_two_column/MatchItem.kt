package com.io.match_between_two_column

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.io.core.ui.LocalPaletteColors
import com.io.core.ui.ProjectTheme
import com.io.core.ui.ProjectTheme.dimens
import com.io.item.rippleBackground

@Composable
internal fun MatchItem(
    modifier: Modifier = Modifier,
    selectedItemId: State<Long?>,
    item: MatchedItem,
    isFound: Boolean,
    matchItem: (MatchedItem) -> Unit,
){
    val shapes = MaterialTheme.shapes
    val palette = LocalPaletteColors.current
    val dimens = dimens

    val supportModifier = remember(isFound) {
        if (isFound){
            return@remember Modifier.background(palette.success)
        }
        Modifier.border(
            dimens.defaultBorder,
            palette.backgroundPrimary,
            shapes.medium,
        )
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
                color = palette.buttonRippleOnContent,
                onClick = { matchItem(item) },
            )
            .padding(ProjectTheme.dimens.insidePadding)
    ){
        Text(
            text = item.text,
            color = palette.textSecondary
        )
    }
}