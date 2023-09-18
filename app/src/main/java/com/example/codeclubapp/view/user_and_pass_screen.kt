package com.example.codeclubapp.view

import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.codeclubapp.EmailPasswordActivity
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyButton
import com.example.codeclubapp.components.MyCodeClubImage
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.components.MyTextPasswordInput
import com.example.codeclubapp.repository.TeacherRepository
import com.example.codeclubapp.ui.theme.GreenLightCode
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//aluno entrar com usuario e senha
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAndPassStudent(navController: NavController){
    /*
    //apenas o usuario do tipo aluno tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }
     */

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
            MyLoginButton(text = "entrar", /*route = "student", navController = navController,*/ modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                /*onValueChange = {
                    loginStudent
                    loginTeacher
                },
                isLoginGoogle = false,*/
                onClick = {

                })
        }
    }

}

//**************************************** THEACHER ****************************************

//professor entrar com usuario e senha
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAndPassTeacher(navController: NavController){

    //utilizar firebase auth
    val auth = FirebaseAuth.getInstance()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    //iniciar repositorio para salvar os dados no bd
    val teacherRepository = TeacherRepository()

    //se salvou ou nao
    var save = false

    /*
    //apenas o usuario do tipo professor tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }
     */

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
            MyLoginButton(text = "criar conta", /*route = "teacher", navController = navController,*/ modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),/*
                onValueChange = {
                    loginStudent
                    loginTeacher
                },
                isLoginGoogle = false,
                */
                onClick = {


                    //verificações do login usando coroutines scope
                    scope.launch(Dispatchers.IO){
                        //verificar o estado dos campos
                        if(userState.isEmpty() || password.value.isEmpty()){
                            //email.isEmpty() || pass.isEmpty()
                            save = false
                        } else if(userState.isNotEmpty() && password.value.isNotEmpty()){
                            var pass = password.value.toString()
                            auth.createUserWithEmailAndPassword(userState, pass).addOnCompleteListener{
                                //resultado do cadastro
                                crud ->
                                if(crud.isSuccessful){
                                    save = true
                                    var name = auth.currentUser.toString()
                                    teacherRepository.saveTeacher( name = name, userState, pass, true, false)
                                } else {
                                    save = false
                                }
                            }.addOnFailureListener{
                                //tratamento de exceções -> mensagens de erro
                                    exception ->
                                val errorMensage = when(exception) {
                                    is FirebaseAuthEmailException -> "digite um email válido"
                                    is FirebaseAuthInvalidCredentialsException -> "digite um email válido"
                                    is FirebaseAuthWeakPasswordException -> "sua senha precisa ter pelo menos 6 caracteres"
                                    is FirebaseAuthUserCollisionException -> "essa conta já foi cadastrada"
                                    is FirebaseNetworkException -> "problemas com a internet"
                                    else -> "erro ao cadastrar usuário"
                                }
                                save = false
                                Toast.makeText(context, " $errorMensage", Toast.LENGTH_SHORT).show()
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
                           Toast.makeText(context, "algo deu errado" , Toast.LENGTH_SHORT).show()
                       }
                    }
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFormTeacher(navController: NavController){

    //utilizar firebase auth
    val auth = FirebaseAuth.getInstance()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    //iniciar repositorio para salvar os dados no bd
    val teacherRepository = TeacherRepository()

    //se salvou ou nao
    var save = false

    /*
    //apenas o usuario do tipo professor tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }
    */

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
            MyLoginButton(text = "entrar",
                modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                onClick = {
                    //verificações do login usando coroutines scope
                    scope.launch(Dispatchers.IO){
                        //verificar o estado dos campos
                        if(userState.isEmpty() || password.value.isEmpty()){
                            //email.isEmpty() || pass.isEmpty()
                            save = false
                        } else if(userState.isNotEmpty() && password.value.isNotEmpty()){
                            var pass = password.value.toString()
                            auth.signInWithEmailAndPassword(userState, pass).addOnCompleteListener{
                                //resultado do cadastro
                                    crud ->
                                if(crud.isSuccessful){
                                    save = true
                                    var name = auth.currentUser.toString()
                                } else {
                                    save = false
                                }
                            }.addOnFailureListener{
                                //tratamento de exceções -> mensagens de erro
                                    exception ->
                                val errorMensage = when(exception) {
                                    is FirebaseAuthEmailException -> "digite um email válido"
                                    is FirebaseAuthInvalidCredentialsException -> "digite um email válido"
                                    is FirebaseAuthWeakPasswordException -> "senha inválida"
                                    is FirebaseAuthUserCollisionException -> "essa conta não existe"
                                    is FirebaseNetworkException -> "problemas com a internet"
                                    else -> "erro ao cadastrar usuário"
                                }
                                save = false
                                Toast.makeText(context, " $errorMensage", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }

                    //mostrar mensagem usando o escopo do app -> context Main
                    scope.launch(Dispatchers.Main){
                        if(save == true){
                            println("\nsalvo com sucesso \n")
                            navController.navigate("teacher")
                            Toast.makeText(context, "tudo ok", Toast.LENGTH_SHORT).show()
                        } else {
                            println("\nalgo deu errado \n")
                            Toast.makeText(context, "algo deu errado ao fazer login, tente novamente" , Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }
}
