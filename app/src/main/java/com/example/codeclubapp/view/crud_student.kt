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
import com.example.codeclubapp.components.MyGroupCheckBox
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.components.MyTextPasswordInput
import com.example.codeclubapp.model.Feed
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import com.example.codeclubapp.repository.StudentRepository
import com.example.codeclubapp.repository.TeacherRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageStudents(navController: NavController){
    //student atributos:
    /*
    var name: String? = null,
    var password: String? = null,
    var email: String? = null,
    var projects: List<Project>? = null,
    var teams: List<Team>? = null,
    var isTeacher: Boolean = false,
    var isStudent: Boolean = true
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

    //criar estado para as caixas de texto:
    var userState by remember {
        //iniciar como uma string vazia
        mutableStateOf("")
    }

    //estado do input -> se digitou a senha
    //val password = remember -> assim preciso usar .value para acessar o valor da variavel
    val password = remember {
        //a senha começa como uma string vazia
        mutableStateOf("")
    }

    //estado da senha -> se ela esta visivel ou nao
    val passwordVisible = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    //utilizar firebase auth
    val auth = FirebaseAuth.getInstance()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()

    val context: Context = LocalContext.current

    //iniciar repositorio para salvar os dados no bd
    val studentRepository = StudentRepository()

    val model = Student()

    //se salvou ou nao
    var save = false
    var isNull = false

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "cadastrar alunos")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "cadastrar aluno(a): ",
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
                value = userState,
                onValueChange = {
                    userState = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                label = "email",
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
                //pass
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
            MyLoginButton(text = "cadastrar", /*route = "teacher", navController = navController,*/ modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.secondary),/*
                onValueChange = {
                    loginStudent
                    loginTeacher
                },
                isLoginGoogle = false,
                */
                onClick = {

                    //verificações do login usando coroutines scope -> criar novo usuario
                    scope.launch(Dispatchers.IO){
                        //verificar o estado dos campos
                        if(userState.isEmpty() || password.value.isEmpty() || nameState.isEmpty()){
                            save = false
                        } else if(userState.isNotEmpty() && password.value.isNotEmpty()){
                            var pass = password.value.toString()
                            auth.createUserWithEmailAndPassword(userState, pass).addOnCompleteListener{
                                //resultado do cadastro
                                    crud ->
                                if(crud.isSuccessful){
                                    save = true
                                    //var name = auth.currentUser.toString()
                                    studentRepository.saveStudent(model.id ,nameState, userState, pass, false, true)
                                } else {
                                    save = false
                                    isNull = true
                                }
                            }.addOnFailureListener{
                                //tratamento de exceções -> mensagens de erro
                                print("erro ao cadastrar aluno")
                                save = false
                                isNull = true
                            }

                        }
                    }

                    //mostrar mensagem usando o escopo do app -> context Main
                    scope.launch(Dispatchers.Main){
                        if(save && !isNull){
                            println("\nestudante cadastrado com sucesso \n")
                            nameState = ""
                            userState = ""
                            password.value = ""
                            navController.navigate("manageStudents")
                            Toast.makeText(context, "salvo com sucesso ", Toast.LENGTH_SHORT).show()
                        } else {
                            println("\nalgo deu errado \n")
                            Toast.makeText(context, "algo deu errado, a senha precisa ter pelo menos 6 caracteres" , Toast.LENGTH_SHORT).show()
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
                text = "alunos(as) cadastrados(as): ",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp

            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(224.dp)
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ){
                //var studentList: MutableList<Student> = mutableListOf()
                val studentList: MutableList<Student> = studentRepository.getStudent().collectAsState(
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
                    itemsIndexed(studentList){
                            position, _ -> MyListStudents(position = position, listItem = studentList, context = context, navController = navController)
                    }
                }

            }
        }
        MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
    }
}

//listar todos os estudantes cadastrados
@Composable
fun MyListStudents(
    position: Int,
    listItem: MutableList<Student>,
    context: Context,
    navController: NavController
){
    val context: Context = LocalContext.current

    //ligar a view com a model
    val nameStudent = listItem[position].name
    val emailStudent = listItem[position].email

    val scope = rememberCoroutineScope()

    val repository = StudentRepository()

    fun deleteDialog(){
        //deletar estudante
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("EXCLUIR ESTUDANTE")
            .setMessage("tem certeza que quer excluir esse estudante ${nameStudent.toString()}?")
            .setPositiveButton("Sim"){
                    _, _, ->

                repository.deleteStudent(emailStudent.toString())

                scope.launch(Dispatchers.Main){
                    //remover estudante excluido da lista
                    listItem.removeAt(position)
                    //navegar para a pagina feed para atualizar a listagem
                    navController.navigate("manageStudents")
                    Toast.makeText(context, "estudante excluído com sucesso!", Toast.LENGTH_SHORT).show()
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
            // Create references for the composables to constrain
            val(
                txtName,
                txtEmail,
                navBarItemEdit,
                navBarItemDelete
            ) = createRefs()

            Text(
                text = nameStudent.toString(),
                modifier = Modifier.constrainAs(txtName) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                text = emailStudent.toString(),
                modifier = Modifier.constrainAs(txtEmail) {
                    top.linkTo(txtName.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.constrainAs(navBarItemEdit) {
                    top.linkTo(txtEmail.bottom, margin = 15.dp)
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
                    top.linkTo(txtEmail.bottom, margin = 15.dp)
                    start.linkTo(navBarItemEdit.end, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
            ) {
                //Text(text = "excluir")
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