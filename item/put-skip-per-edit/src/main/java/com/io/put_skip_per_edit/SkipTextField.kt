package com.io.put_skip_per_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.io.core.ui.ProjectTheme.palette

@Composable
fun SkipTextField(
    inputText: String,
    onTextChange: (String) -> Unit,
    onCheckText: () -> Unit,
    modifier: Modifier = Modifier,
){
    OutlinedTextFieldWithPadding(
        modifier = modifier,
        value = inputText,
        trailingIcon = {
            if (inputText.isNotBlank()){
                SendOnCheckButton (
                    modifier = Modifier
                        .size(30.dp)
                        .padding(vertical = 4.dp),
                    onClick = onCheckText
                )
            }
        },
        onValueChange = onTextChange,
    )
    
}