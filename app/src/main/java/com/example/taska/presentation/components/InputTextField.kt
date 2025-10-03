package com.example.taska.presentation.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.taska.presentation.constants.TextFieldType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InputTextField(
    value: String,
    modifier: Modifier = Modifier,
    cursorDelayAnimation: Long = 500L,
    typeField: TextFieldType = TextFieldType.DEFAULT,
    maxLines: Int = Int.MAX_VALUE,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    autoFocus: Boolean = false,
    onLeaveFocus: () -> Unit = { },
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    BasicTextField(
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        decorationBox = { innerTextField ->
            var isShowCursor by remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    while (value.isEmpty()) {
                        isShowCursor = !isShowCursor
                        delay(cursorDelayAnimation)
                    }
                    isShowCursor = false
                }
            }
            if (value.isEmpty() && enabled) {
                if (isShowCursor && isFocused) {
                    Text(
                        text = "|",
                        fontSize = typeField.fontSize.sp
                    )
                }
                Text(
                    text = typeField.text,
                    fontSize = typeField.fontSize.sp,
                    color = typeField.textColor.copy(alpha = 0.4f)
                )
            } else innerTextField()
        },
        textStyle = TextStyle.Default.copy(
            fontSize = typeField.fontSize.sp,
            color = typeField.textColor,
            fontWeight = typeField.fontWeight
        ),
        maxLines = maxLines,
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                val nowFocused = state.isFocused
                if (!state.isFocused) onLeaveFocus()
                isFocused = nowFocused
            }
    )
}