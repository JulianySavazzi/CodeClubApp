package com.example.codeclubapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.codeclubapp.ui.theme.GreenLightCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextPasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    label: String,
    maxLines: Int,
    passwordVisible: MutableState<Boolean>
){
/*
    //estado do input -> se digitou a senha
    val password = remember {
        //a senha começa como uma string vazia
        mutableStateOf("")
    }

    //estado da senha -> se ela esta visivel ou nao
    val passwordVisible = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    value = password.value,
    onValueChange = {
        password.value = it
    },
*/
    OutlinedTextField(
        //value: String -> texto digitado no input
        value,
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
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),

        trailingIcon = {
            val iconImage =
                if(value == ""){
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            } ){
                Icon(imageVector = iconImage, contentDescription = "")
            }
        },
        visualTransformation = if(passwordVisible.value == true) VisualTransformation.None else PasswordVisualTransformation()

    )

}

