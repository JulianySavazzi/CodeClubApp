package com.example.codeclubapp.view

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.example.codeclubapp.R
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyGroupCheckBox
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.model.Feed
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import com.example.codeclubapp.repository.FeedRepository
import com.example.codeclubapp.repository.ProjectRepository
import com.example.codeclubapp.repository.StudentRepository
import com.example.codeclubapp.repository.TeamRepository
import com.example.codeclubapp.ui.theme.WHITE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Array
import java.util.Arrays

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

    val feedRepository = FeedRepository()

    val feedModel = Feed()

    val model = Team()

    //se salvou ou nao
    var save = false
    var isNull = false

    //adicionar projeto selecionado nessa lista para salvar no banco
    val myProjects: MutableList<Project> = mutableListOf()

    //adicionar estudante selecionado nessa lista para salvar no banco
    val myMembers: MutableList<Student> = mutableListOf()

    //var i: Int = myMembers!!.size
    //var j: Int = myProjects!!.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "cadastrar equipes")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
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
                label = "nome da equipe",
                maxLines = 1
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp)
                //.fillMaxHeight()
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ){

                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = "projetos: ",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    )
                }


                //preencher a lista
                //var projectList: MutableList<Project> = mutableListOf()
                val projectList: MutableList<Project> = projectRepository.getProject().collectAsState(
                    //se o estado da lista for vazio vai retornar uma mutableListOf
                    //se a lista tiver preenchida vai retornar os valores dos documentos
                    mutableListOf()
                ).value

                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = "selecionados: ${myProjects.size}",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    )
                }

                //componente de listagem
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    itemsIndexed(projectList){
                            position, _ -> MyCheckListProjects(position = position, listItem = projectList, selectedItem = myProjects, context = context)
                    }
                }

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(168.dp)
            //.fillMaxHeight(0.5f)
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ){

                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = "membros:",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    )
                }

                //preencher lista
                val studentList: MutableList<Student> = studentRepository.getStudent().collectAsState(
                    //se o estado da lista for vazio vai retornar uma mutableListOf
                    //se a lista tiver preenchida vai retornar os valores dos documentos
                    mutableListOf()
                ).value

                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = "selecionados: ${myMembers.size}",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp

                    )
                }

                //componente de listagem
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    itemsIndexed(studentList){
                            position, _ -> MyCheckListMembers(position = position, listItem = studentList, selectedItem = myMembers, context = context) }
                }

            }
        }

        var idTeam = teamRepository.getTeamByName(nameState).id

        var nameProjects: MutableList<String> = mutableListOf()
        var nameMembers: MutableList<String> = mutableListOf()

        Row (
            modifier = Modifier
                .fillMaxWidth()
                //.height(56.dp)
                .padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyLoginButton(text = "cadastrar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                onClick = {

                    //verificações do login usando coroutines scope -> criar nova equipe
                    scope.launch(Dispatchers.IO){
                        //verificar o estado dos campos
                        if(nameState.isEmpty() ){
                            //verify-> name, project, member
                            save = false
                        } else if(nameState.isNotEmpty() && myProjects.isNotEmpty() && myMembers.isNotEmpty()){
                            if(teamRepository.getTeamByName(nameState).id != model.id){
                                teamRepository.saveTeam(model.id, nameState, myMembers, myProjects, model.vote)
                                println("team is not null, id: $idTeam != ${model.id}, $myMembers , $myProjects")

                                for (j in myProjects.indices) {
                                    nameProjects.add(myProjects[j].name.toString())
                                }

                                for(i in myMembers.indices){
                                    nameMembers.add(myMembers[i].name.toString())
                                }

                                feedRepository.saveFeed(
                                    feedModel.id,
                                    "nova equipe cadastrada: $nameState - id ${model.id}", "nova equipe cadastrada: \n nome: $nameState \n projetos: ${listOf(nameProjects.toString())} ...\n membros: ${listOf(nameMembers.toString())}"
                                )
                                save = true
                                isNull = false
                            } else {
                                save = false
                                isNull = true
                                print("team is null,  id: $idTeam, model.id: ${model.id}")
                            }
                        }
                    }

                    //mostrar mensagem usando o escopo do app -> context Main
                    scope.launch(Dispatchers.Main){
                        if(save == true && isNull == false){
                            println("\nequipe salva com sucesso \n")
                            navController.navigate("teacher")
                            Toast.makeText(context, "salvo com sucesso ", Toast.LENGTH_SHORT).show()
                        } else {
                            println("\nalgo deu errado \n")
                            Toast.makeText(context, "algo deu errado, preencha todos os campos!" , Toast.LENGTH_SHORT).show()
                            navController.navigate("teacher")
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
                .padding(15.dp),
                //.height(56.dp)
                //.padding(20.dp, 30.dp, 20.dp, 20.dp),
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
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(122.dp)
                //.fillMaxHeight(0.5f)
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ){

                //preencher lista
                val teamsList: MutableList<Team> = teamRepository.getTeam().collectAsState(
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
                    itemsIndexed(teamsList){
                            position, _ -> MyListTeams(position = position, listItem = teamsList, context = context, navController = navController) }
                }

            }
        }
        MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
    }
}


//get checkbox project selected
/*
fun mySelectedProjects(mutableListProjects: MutableList<Project>): StateFlow<MutableList<Project>>{
    //flow -> recuperar todo fluxo de PROJECT
    //_allProjects -> estado de fluxo assincrono
    val _listProjects = MutableStateFlow<MutableList<Project>>(mutableListOf())
    //allProjects -> observa todos os dados que foram atribuidos para ela
    val listProjects: StateFlow<MutableList<Project>> = _listProjects
    _listProjects.value = mutableListProjects
    return  listProjects
}
 */

//CHECKBOX PROJECTS
@Composable
fun MyCheckListProjects(
    position: Int,
    listItem: MutableList<Project>,
    context: Context,
    //navController: NavController,
    selectedItem: MutableList<Project>
){
    //ligar a view com a model
    val nameProject = listItem[position].name

    val scope = rememberCoroutineScope()

    //val navController: NavController

    var selected by remember {
        mutableStateOf(false)
    }

    //var myProjectsList: State<List<Project>>

    fun removeSelected(){
        scope.launch(Dispatchers.Main){
        //remover projeto excluido da lista
            if(selectedItem.size > 0) {
                selectedItem.removeAt(position)
                Toast.makeText(context, "item removido" , Toast.LENGTH_SHORT).show()
            } else {
                //navController.navigate("manageTeams")
                Toast.makeText(context, "não foi possiível remover o item" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    Divider(
        thickness = 10.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
    Card(
        modifier = Modifier.padding(5.dp
        )
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(5.dp)
        ) {
            // Create references for the composables to constraint
            val(
                txtName,
                check,
                navBarRemove
            ) = createRefs()


            Text(
                text = nameProject.toString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                modifier = Modifier
                    .constrainAs(txtName) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(check.end, margin = 10.dp)
                    }
                    .padding(start = 16.dp)
            )



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

            IconButton(
                onClick = {
                    removeSelected()
                },
                modifier = Modifier.constrainAs(navBarRemove) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(txtName.end, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
            ) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.icon_delete_24), contentDescription ="excluir")
            }

        }
    }
    Divider(
        thickness = 10.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
}

//moatrar lista de equipes cadastradas
@Composable
fun MyCheckListMembers(
    position: Int,
    listItem: MutableList<Student>,
    context: Context,
    selectedItem: MutableList<Student>
){

    //ligar a view com a model
    val memberTeam = listItem[position].name

    var selected by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    fun removeSelected(){
        scope.launch(Dispatchers.Main){
            //remover projeto excluido da lista
            if(selectedItem.size > 0) {
                selectedItem.removeAt(position)
                Toast.makeText(context, "item removido" , Toast.LENGTH_SHORT).show()
            } else {
                //navController.navigate("manageTeams")
                Toast.makeText(context, "não foi possiível remover o item" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    Divider(
        thickness = 10.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
    Card(
        modifier = Modifier.padding(5.dp
        )
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(5.dp)
        ) {
            // Create references for the composables to constrain
            val(
                txtTitle,
                check,
                navBarRemove
            ) = createRefs()

            Text(
                text = memberTeam.toString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                modifier = Modifier.constrainAs(txtTitle) {
                    top.linkTo(parent.top, margin = 10.dp)
                    start.linkTo(check.end, margin = 10.dp)
                }
            )

            Checkbox(
                checked = selected,
                onCheckedChange =  {
                        selected_ ->
                    selected = selected_
                    //adicionar item selecionado na lista
                    //selected = true
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

            IconButton(
                onClick = {
                    removeSelected()
                },
                modifier = Modifier.constrainAs(navBarRemove) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(txtTitle.end, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
            ) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.icon_delete_24), contentDescription ="excluir")
            }

        }
    }
    Divider(
        thickness = 10.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
}

//moatrar lista de equipes cadastradas
@Composable
fun MyListTeams(
    position: Int,
    listItem: MutableList<Team>,
    context: Context,
    navController: NavController
){
   // val context: Context = LocalContext.current
    val scope = rememberCoroutineScope()

    val repository = TeamRepository()

    //ligar a view com a model
    val titleTeam = listItem[position].name
    val projectsTeam = listItem[position].projects
    val membersTeam = listItem[position].members

    var nameProjects: MutableList<String> = mutableListOf()
    var nameMembers: MutableList<String> = mutableListOf()

    var i: Int = membersTeam!!.size
    var j: Int = projectsTeam!!.size

    fun deleteDialog(){
        //deletar estudante
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("EXCLUIR EQUIPE")
            .setMessage("tem certeza que quer excluir essa equipe ?")
            .setPositiveButton("Sim"){
                    _, _, ->
                //funcao delete
                repository.deleteTeam(titleTeam.toString())

                scope.launch(Dispatchers.Main){
                    //remover equipe excluido da lista
                    listItem.removeAt(position)
                    //navegar para a pagina para atualizar a listagem
                    navController.navigate("manageTeams")
                    Toast.makeText(context, "equipe excluída com sucesso!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Não"){
                    _, _, ->
            }
            .show()
    }

    for (j in projectsTeam.indices) {
        nameProjects.add(projectsTeam[j].name.toString())
    }

    for(i in membersTeam.indices){
     //selectedItem.add(listItem[position])
        nameMembers.add(membersTeam[i].name.toString())
    }

    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
    Card(
        modifier = Modifier.padding(15.dp
        )
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(15.dp)
        ) {
            // Create references for the composables to constrain
            val(
                txtTitle,
                txtProjects,
                txtMembers,
                navBarItemEdit,
                navBarItemDelete
            ) = createRefs()

            Text(
                text = "equipe: ${titleTeam.toString()}",
                modifier = Modifier.constrainAs(txtTitle) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                //text = "projetos: ${projectsTeam!![0].name.toString()} ...",
                text = "projetos: ${listOf(nameProjects.toString())} ",
                modifier = Modifier.constrainAs(txtProjects) {
                    top.linkTo(txtTitle.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                //Arrays.toString(arrayName)
                text = "membros: ${listOf(nameMembers.toString())} ",
                modifier = Modifier.constrainAs(txtMembers) {
                    top.linkTo(txtProjects.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            IconButton(
                onClick = {
                    /*TODO*/
                    println(" * testando funcao de atualizar votos * ")
                    //repository.updateVoteTeamByName(titleTeam.toString(), 10)
                          },
                modifier = Modifier.constrainAs(navBarItemEdit) {
                    top.linkTo(txtMembers.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                },
            ) {
                //Text(text = "editar"),
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.icon_edit_24), contentDescription ="editar")
            }

            IconButton(
                onClick = {
                    deleteDialog()
                },
                modifier = Modifier.constrainAs(navBarItemDelete) {
                    top.linkTo(txtMembers.bottom, margin = 15.dp)
                    start.linkTo(navBarItemEdit.end, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
            ) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.icon_delete_24), contentDescription ="excluir")
            }


        }
    }
    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
}

