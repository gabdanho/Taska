package com.example.taska.ui.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.taska.constants.TextFieldType

@Composable
fun InputTextField(
    typeField: TextFieldType = TextFieldType.DEFAULT,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = typeField.text,
                fontSize = typeField.fontSize.sp
            )
        },
        textStyle = TextStyle.Default.copy(
            fontSize = typeField.fontSize.sp,
            fontWeight = if (typeField == TextFieldType.TITLE) FontWeight.W400 else FontWeight.Normal
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun InputFieldPreview() {
    InputTextField(
        typeField = TextFieldType.TITLE,
        value = "",
        onValueChange = { }
    )
}