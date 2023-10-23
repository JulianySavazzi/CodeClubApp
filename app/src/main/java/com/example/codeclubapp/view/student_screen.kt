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
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.repository.ProjectRepository
import com.example.codeclubapp.repository.StudentRepository
import com.example.codeclubapp.repository.TeamRepository
import com.example.codeclubapp.ui.theme.GreenLightCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Student(navController: NavController){
    //apenas o usuario do tipo aluno tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    //utilizar firebase auth
    val auth = FirebaseAuth.getInstance()

    val teamsRepo = TeamRepository()

    val projectRepo = ProjectRepository()

    val repository = StudentRepository()

    val student: Student = repository.getSudentByEmail(auth.currentUser?.email.toString())

    //var nameStudent = student.name

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "aluno(a)")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()) //barra de rolagem
        ) {
            //Rows -> corpo do app
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                Text(
                    text = "meus projetos: ",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )
            }
            Divider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                //Arrays.toString(arrayName)
                Text(
                    text = "minhas equipes: ${teamsRepo.getStudentTeam(student.name.toString())}",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )
            }
            Divider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(),
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
                    text = "meu perfil:\n email: ${Firebase.auth.currentUser?.email}\n nome: ${student.name}",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                MyAppBarBottom(
                    navController = navController,
                    loginStudent = loginStudent,
                    loginTeacher = loginTeacher
                )
            }
        }
    }
}