package com.example.codeclubapp.view

import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyGroupCheckBox
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.repository.ProjectRepository
import com.example.codeclubapp.repository.StudentRepository
import com.example.codeclubapp.repository.TeamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTeams(navController: NavController){

    /*
    var name: String? = null,
    var members: MutableList<Student>? = null,
    var projects: MutableList<Project>? = null
    */

    //apenas o usuario do tipo professor tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    //criar estado para as caixas de texto -> titulo e conteudo
    var nameState by remember {
        mutableStateOf("")
    }

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    //iniciar repositorio para salvar os dados no bd
    val teamRepository = TeamRepository()

    val studentRepository = StudentRepository()

    val projectRepository = ProjectRepository()

    //se salvou ou nao
    var save = false

    val project: MutableList<Project> = mutableListOf()

    val student: MutableList<Student> = mutableListOf()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            //.verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "cadastrar equipes")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "cadastrar equipe: ",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp

            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyTextBoxInput(
                value = nameState,
                onValueChange = {
                    nameState = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = "nome",
                maxLines = 1
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            //MyGroupCheckBox(name = nameState, members = student, projects = project, repository = teamRepository, context = context, scope = scope, navController = navController)
            MyGroupCheckBox(name = projectRepository.getProject().toString(), onClick = {})
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            //MyGroupCheckBox(name = nameState, members = student, projects = project, repository = teamRepository, context = context, scope = scope, navController = navController)
            MyGroupCheckBox(name = studentRepository.getStudent().toString(), onClick = {})
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyLoginButton(text = "cadastrar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                onClick = {

                    //verificações do login usando coroutines scope -> criar novo usuario
                    scope.launch(Dispatchers.IO){
                        //verificar o estado dos campos
                        if(nameState.isEmpty() ){
                            //verify-> name, project, member
                            save = false
                        } else if(nameState.isNotEmpty() ){
                            save = true
                            teamRepository.saveTeam(0, nameState, student, project)
                            if(teamRepository.getTeamByName(nameState) != ""){
                                println("team is not null")
                            } else {
                                save = false
                                print("team is null")
                            }
                        }
                    }

                    //mostrar mensagem usando o escopo do app -> context Main
                    scope.launch(Dispatchers.Main){
                        if(save == true){
                            println("\nsalvo com sucesso \n")
                            navController.navigate("teacher")
                            Toast.makeText(context, "salvo com sucesso ", Toast.LENGTH_SHORT).show()
                        } else {
                            println("\nalgo deu errado \n")
                            Toast.makeText(context, "algo deu errado, preencha todos os campos!" , Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
        Divider(
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "equipes cadastradas: ",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp

            )
        }
        MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
    }
}