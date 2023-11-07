package com.example.codeclubapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyButton
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.ui.theme.GreenLightCode
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Teacher(navController: NavController){
    //apenas o usuario do tipo professor tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "professor(a)")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "gerenciar cadastros",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp

            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()) //barra de rolagem
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                MyLoginButton(
                    text = "gerenciar alunos(as)",
                    //route = "manageStudents",
                    //navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    //onValueChange = {
                    //    loginTeacher.value = true
                    //    print("try go manageStudents")
                    //}
                    onClick = {
                        loginTeacher.value = true
                        navController.navigate("manageStudents")
                        print("try go manageStudents")
                    }
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                MyLoginButton(text = "gerenciar projetos",
                    //route = "manageProjects",
                    //navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    //onValueChange = {

                    //}
                    onClick = {
                        loginTeacher.value = true
                        navController.navigate("manageProjects")
                        print("try go manageProjects")
                    }
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                MyLoginButton(text = "gerenciar equipes",
                    //route = "manageTeams",
                    //navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        loginTeacher.value = true
                        navController.navigate("manageTeams")
                        print("try go manageTeams")
                    }
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                MyLoginButton(
                    text = "gerenciar votações",
                    //route = "managePolls",
                    //navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        loginTeacher.value = true
                        navController.navigate("managePolls")
                        print("try go managePolls")
                    }
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                MyLoginButton(
                    text = "cadastrar publicações",
                    //route = "manageFeed",
                    //navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    //onValueChange = {
                    //    loginTeacher.value = true
                    //    print("try go manageFeed")
                    //}
                    onClick = {
                        loginTeacher.value = true
                        navController.navigate("manageFeed")
                        print("manageFeed")
                    }
                )
            }
            Divider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                Text(
                    text = "perfil",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                MyLoginButton(text = "sair do app",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(GreenLightCode),
                    onClick = {
                        //fazer logout
                        Firebase.auth.signOut()
                        if(Firebase.auth.currentUser == null){
                            print("sing out")
                            navController.navigate("home")
                        } else {
                            Firebase.auth.signOut()
                        }
                        //print("sing out")
                        //navController.navigate("home")
                    })
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
        }
    }
}