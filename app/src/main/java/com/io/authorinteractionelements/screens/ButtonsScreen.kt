package com.io.authorinteractionelements.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.io.authorinteractionelements.R
import com.io.authorinteractionelements.Screen
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.ProjectTheme.palette

@Composable
fun ButtonsScreen(
    modifier: Modifier,
    changeScreen: (Screen) -> Unit
) {
    Column(
        modifier = modifier
            .padding(dimens.horizontalPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Screen.values().forEachIndexed { index, screen ->
            Button(
                shape = shapes.large,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = palette.backgroundSecondary
                ),
                onClick = { changeScreen(screen) },
            ) {
                Text(
                    text = stringResource(screen.label),
                    color = palette.contentSecondary
                )
            }

            if (index != Screen.values().lastIndex){
                Spacer(modifier = Modifier.height(dimens.smallSpace))
            }
        }
    }
}