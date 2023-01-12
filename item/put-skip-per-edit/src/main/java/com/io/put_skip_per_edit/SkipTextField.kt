package com.io.put_skip_per_edit

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.io.core.ui.ProjectTheme.palette

@Composable
fun SkipTextField(
    modifier: Modifier = Modifier,
    inputText: String,
    onTextChange: (String) -> Unit,
    onCheckText: () -> Unit,
    isError: MutableState<Boolean> = mutableStateOf(false),
    readOnly: Boolean = false,
){
    OutlinedTextFieldWithPadding(
        modifier = modifier,
        value = inputText,
        readOnly = readOnly,
        isError = isError,
        trailingIcon = {
            if (inputText.isNotBlank() && !readOnly){
                SendOnCheckButton (
                    modifier = Modifier,
                    onClick = onCheckText
                )
            }
        },
        onValueChange = onTextChange,
    )
    
}