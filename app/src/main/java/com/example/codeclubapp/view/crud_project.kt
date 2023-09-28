package com.example.codeclubapp.view

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.codeclubapp.R
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.repository.ProjectRepository
import com.example.codeclubapp.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProjects(navController: NavController){
    //Project Model:
    //var name: String
    //var description: String

    //criar estado para as caixas de texto -> titulo e conteudo
    var nameState by remember {
        mutableStateOf("")
    }

    //criar estado para as caixas de texto:
    var descriptionState by remember {
        //iniciar como uma string vazia
        mutableStateOf("")
    }

    //apenas o usuario do tipo professor tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()

    val context: Context = LocalContext.current

    //iniciar repositorio para salvar os dados no bd
    val repository = ProjectRepository()

    val model = Project()

    //se salvou ou nao
    var save = false

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "cadastrar projetos")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "cadastrar projeto: ",
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
                label = "nome do projeto",
                maxLines = 1
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            //inserir usuario
            MyTextBoxInput(
                //email
                value = descriptionState,
                onValueChange = {
                    descriptionState = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                label = "descrição",
                maxLines = 1
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyLoginButton(text = "cadastrar", /*route = "teacher", navController = navController,*/ modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.secondary),
                onClick = {

                    //verificações do login usando coroutines scope -> criar novo usuario
                    scope.launch(Dispatchers.IO){
                        //verificar o estado dos campos
                        if(nameState.isEmpty() || descriptionState.isEmpty() ){
                            save = false
                        } else if(nameState.isNotEmpty() && descriptionState.isNotEmpty()){
                            if(repository.getProjectByName(nameState, descriptionState).id != model.id){
                                repository.saveProject(model.id, nameState, descriptionState)
                                save = true
                                print("project save is true")
                            } else {
                                save = false
                                print("project save is false")
                            }
                        }
                    }

                    //mostrar mensagem usando o escopo do app -> context Main
                    scope.launch(Dispatchers.Main){
                        if(save == true){
                            println("\nprojeto cadastrado com sucesso \n")
                            nameState = ""
                            descriptionState = ""
                            navController.navigate("manageProjects")
                            Toast.makeText(context, "projeto salvo com sucesso ", Toast.LENGTH_SHORT).show()
                        } else {
                            println("\nalgo deu errado \n")
                            Toast.makeText(context, "algo deu errado, preencha todos os campos" , Toast.LENGTH_SHORT).show()
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
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "projetos cadastrados: ",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp

            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .height(280.dp)
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ){
                //var projectList: MutableList<Project> = mutableListOf()
                val projectList: MutableList<Project> = repository.getProject().collectAsState(
                    //se o estado da lista for vazio vai retornar uma mutableListOf
                    //se a lista tiver preenchida vai retornar os valores dos documentos
                    mutableListOf()
                ).value

                //preencher lista

                //componente de listagem
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    itemsIndexed(projectList){
                            position, _ -> MyListProjects(position = position, listItem = projectList, context = context, navController = navController)
                    }
                }

            }
        }
        MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
    }
}

//listar todos os projetos cadastrados
@Composable
fun MyListProjects(
    position: Int,
    listItem: MutableList<Project>,
    context: Context,
    navController: NavController
){
    //ligar a view com a model
    val nameProject = listItem[position].name
    val descriptionProject = listItem[position].description

    val scope = rememberCoroutineScope()

    val repository = ProjectRepository()

    fun deleteDialog(){
        //deletar estudante
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("EXCLUIR PROJETO")
            .setMessage("tem certeza que quer excluir esse projeto ${nameProject.toString()}?")
            .setPositiveButton("Sim"){
                    _, _, ->

                repository.deleteProject(nameProject.toString(), descriptionProject.toString())

                scope.launch(Dispatchers.Main){
                    //remover estudante excluido da lista
                    listItem.removeAt(position)
                    //navegar para a pagina feed para atualizar a listagem
                    navController.navigate("manageProjects")
                    Toast.makeText(context, "projeto excluído com sucesso!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Não"){
                    _, _, ->
            }
            .show()
    }

    Divider(
        thickness = 15.dp,
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
                txtDescription,
                navBarItemEdit,
                navBarItemDelete
            ) = createRefs()

            Text(
                text = nameProject.toString(),
                modifier = Modifier.constrainAs(txtName) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                text = descriptionProject.toString(),
                modifier = Modifier.constrainAs(txtDescription) {
                    top.linkTo(txtName.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.constrainAs(navBarItemEdit) {
                    top.linkTo(txtDescription.bottom, margin = 15.dp)
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
                    top.linkTo(txtDescription.bottom, margin = 15.dp)
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