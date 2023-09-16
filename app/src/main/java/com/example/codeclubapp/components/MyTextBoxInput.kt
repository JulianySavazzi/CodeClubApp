@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.codeclubapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.codeclubapp.ui.theme.GreenLightCode

@ExperimentalMaterial3Api
@Composable
fun MyTextBoxInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    label: String,
    maxLines: Int
){
    OutlinedTextField(
        //value: String -> texto digitado no input
        value = value,
        onValueChange,
        modifier,
        label = {
            Text(
                text = label,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
                )
        },
        maxLines = maxLines,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onBackground,
            focusedBorderColor = GreenLightCode,
            focusedLabelColor = GreenLightCode
        )

    )

}

//teste
@Composable
@Preview
private fun MyTextBoxInputPreview(){
MyTextBoxInput(
    value = "oii",
    onValueChange = {},
    modifier = Modifier.fillMaxWidth(),
    label = "mensagem",
    maxLines = 1)
}