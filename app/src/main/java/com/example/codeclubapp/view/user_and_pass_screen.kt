package com.example.codeclubapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyButton
import com.example.codeclubapp.components.MyCodeClubImage
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.components.MyTextPasswordInput

//aluno entrar com usuario e senha
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAndPassStudent(navController: NavController){
    //criar estado para as caixas de texto:
    var userState by remember {
        //iniciar como uma string vazia
        mutableStateOf("")
    }

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


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "entrar no app")
        Row (
            modifier = Modifier
                .fillMaxWidth(2f)
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyCodeClubImage()
        }
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            //inserir usuario
            MyTextBoxInput(
                value = userState,
                onValueChange = {
                                userState = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                label = "usuário",
                maxLines = 1
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //inserir senha
            MyTextPasswordInput(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                label = "senha",
                maxLines = 1,
                passwordVisible = passwordVisible
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyButton(text = "entrar", route = "student", navController = navController, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp))
        }
    }

}

//professor entrar com usuario e senha
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAndPassTeacher(navController: NavController){

    //criar estado para as caixas de texto:
    var userState by remember {
        //iniciar como uma string vazia
        mutableStateOf("")
    }

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "entrar no app")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth(2f)
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyCodeClubImage()
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            //inserir usuario
            MyTextBoxInput(value = userState,
                onValueChange = {
                                userState = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                label = "usuário",
                maxLines = 1
                )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //inserir senha
            MyTextPasswordInput(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                label = "senha",
                maxLines = 1,
                passwordVisible = passwordVisible
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyButton(text = "entrar", route = "teacher", navController = navController, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp))
        }
    }

}

//*********************** VERIFY AUTH ***********************
fun verifyLogin( login_ok: Boolean): Boolean{
    return login_ok
}