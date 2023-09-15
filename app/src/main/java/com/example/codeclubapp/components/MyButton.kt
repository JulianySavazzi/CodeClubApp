package com.example.codeclubapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.codeclubapp.ui.theme.WHITE

//botao
@Composable
fun MyButton(
    //variaveis que serao passadas para o botao
    text: String,
    route: String,
    navController: NavController
) {
    Button(onClick = {
        //passar variavel com a rota para mudar de tela
        navController.navigate(route = route)
    }) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Clip,
            color = WHITE,
            fontSize = 18.sp
            )
        Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(56.dp)
    }
}
