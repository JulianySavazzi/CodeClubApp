package com.example.codeclubapp.view

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import com.example.codeclubapp.repository.TeamRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Poll(navController: NavController){
    //o usuario nao precisa estar logado, so precisa ter o codigo de autenticacao
    //entao nao sera nem aluno nem professor
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    //adicionar equipe selecionada nessa lista para salvar no banco -> contabilizar as votações
    val myTeams: MutableList<Team> = mutableListOf()

    val teamRepository = TeamRepository()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            //.verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "votação")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(56.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                text = "escolha alguém para votar:",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp

            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                //.height(122.dp)
                .fillMaxHeight(0.90f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ) {
                //preencher a lista
                val teamstList: MutableList<Team> = teamRepository.getTeam().collectAsState(
                    //se o estado da lista for vazio vai retornar uma mutableListOf
                    //se a lista tiver preenchida vai retornar os valores dos documentos
                    mutableListOf()
                ).value

                //componente de listagem
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    itemsIndexed(teamstList) { position, _ ->
                        VotesTeam(
                            position = position,
                            listItem = teamstList,
                            selectedItem = myTeams,
                            context = context,
                            navController = navController
                        )
                    }
                }
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            /*
            MyButton(
                text = "", //nome do projeto
                route = "home", //voto concluido
                navController = navController,
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                onValueChange = {
                    loginStudent
                    loginTeacher
                }
            )
             */
            MyLoginButton(
                text = "votar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                onClick = {
                    //verificar codigo de votacao
                    //confirmar voto em alert -> salvar voto
                    //logout
                    if(myTeams.isNotEmpty()){
                        Toast.makeText(context, "voto salvo com sucesso!" , Toast.LENGTH_SHORT).show()
                        Firebase.auth.signOut()
                        if(Firebase.auth.currentUser == null){
                            print("sing out")
                            navController.navigate("home")
                        } else {
                            Firebase.auth.signOut()
                        }
                    }else{
                        Toast.makeText(context, "selecione uma equipe para votar!" , Toast.LENGTH_SHORT).show()
                    }

                })

        }
    }
}

@Composable
fun VotesTeam(
    position: Int,
    listItem: MutableList<Team>,
    context: Context,
    navController: NavController,
    selectedItem: MutableList<Team>
) {

    var selectedOption: Boolean by remember {
        mutableStateOf(false)
    }

    //ligar a view com a model
    val titleTeam = listItem[position].name
    //val projectsTeam = listItem[position].projects

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current



    //enviar equipe selecionada para a funcao de salvar voto
    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
    Card(
        modifier = Modifier.padding(15.dp
        )
    ){
        ConstraintLayout(
            modifier = Modifier.padding(5.dp)
        ) {
            // Create references for the composables to constrain
            val (
                txtTitle,
                //txtProjects,
                check
            ) = createRefs()

            /*

            var selected by remember {
                mutableStateOf(false)
            }

            Checkbox(
                checked = selected,
                onCheckedChange =  {
                        selected_ ->
                    selected = selected_
                    //selected = true
                    //adicionar item selecionado na lista
                    if(selected){
                        selectedItem.add(listItem[position])
                        print("projeto selecionado: $selectedItem")
                    }
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Red
                ),
                modifier = Modifier
                    .constrainAs(check) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    }
            )

             */

            RadioButton(
                selected = selectedOption,
                onClick = {
                    selectedOption = true
                    //adicionar item selecionado na lista
                    if(selectedOption && selectedItem.size == 0){
                        selectedItem.add(listItem[position])
                        print("equipe selecionada: $selectedItem")
                    } else {
                        selectedOption = false
                        print("já tem uma equipe selecionada: $selectedItem")
                        //usar navigator para atualizar pagina
                        navController.navigate("poll")
                    }
                          },
                modifier = Modifier
                    .constrainAs(check) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    }

                )

            Text(
                text = "$titleTeam",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                modifier = Modifier
                    .constrainAs(txtTitle) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(check.end, margin = 10.dp)
                    }
                    .padding(start = 16.dp)
            )
        }
    }
    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )

}