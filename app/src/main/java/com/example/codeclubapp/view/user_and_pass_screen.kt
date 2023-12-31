package com.example.codeclubapp.view

import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.service.controls.ControlsProviderService.NETWORK_STATS_SERVICE
import android.service.controls.ControlsProviderService.TAG
import android.telephony.AccessNetworkConstants.AccessNetworkType
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyCodeClubImage
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.components.MyTextPasswordInput
import com.example.codeclubapp.model.Teacher
import com.example.codeclubapp.repository.StudentRepository
import com.example.codeclubapp.repository.TeacherRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.FirebaseAppCheckTokenProvider
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.firestore.core.FirestoreClient
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import org.checkerframework.checker.nullness.qual.NonNull
import java.nio.channels.NetworkChannel

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

    //utilizar firebase auth
    val auth = FirebaseAuth.getInstance()

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

    /*
    var verifyStudent by remember {
        //iniciar como uma string vazia
        mutableStateOf(false)
    }
     */

    val repository = StudentRepository()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var er = ""

    //var isNull = false

    //se salvou ou nao
    var save by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "entrar no app estudante")
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
            MyLoginButton(text = "entrar",
                modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp))
            {
                //verifyStudent = repository.isStudent(userState)
                //verificações do login usando coroutines scope
                scope.launch(Dispatchers.IO) {
                    //verificar o estado dos campos
                    if (userState.isEmpty() || password.value.isEmpty()) {
                        //save = false
                        er = "preencha todos os campos!"
                    } else if (userState.isNotEmpty() && password.value.isNotEmpty()) {
                        var pass = password.value
                        repository.verifyStudent(userState, pass)
                        if(auth.currentUser != null) save = true
                    }
                }

                //scope.wait()

                //mostrar mensagem usando o escopo do app -> context Main
                scope.launch(Dispatchers.Main) {
                    if ((save == true) && (auth.currentUser!!.email.toString() == userState)) {
                        println("\nsalvo com sucesso \n")
                        Toast.makeText(context, "tudo ok", Toast.LENGTH_SHORT).show()
                        navController.navigate("student")
                    } else {
                        println("\nalgo deu errado  \n")
                        Toast.makeText(
                            context,
                            "algo deu errado ao fazer login, $er tente novamente...  ",
                            Toast.LENGTH_LONG
                        ).show()
                        //if (isNull == true) navController.navigate("user_student")
                    }
                }
            }
        }
    }

}

//************************************************************************** THEACHER **************************************************************************

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

    val model = Teacher()

    //se salvou ou nao
    var save = false

    var er = ""

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

    var errorMessage = false

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "criar conta")
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
                    //verificações do login usando coroutines scope -> criar novo usuario
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
                                    teacherRepository.saveTeacher( model.id, name = name, userState, pass, true, false)
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
                                errorMessage = true
                                er = errorMensage
                                //Toast.makeText(context, " $errorMensage", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }

                    //mostrar mensagem usando o escopo do app -> context Main
                    scope.launch(Dispatchers.Main){
                       if(save == true && errorMessage == false){
                           println("\nsalvo com sucesso \n")
                           Toast.makeText(context, "cadastrado com sucesso ", Toast.LENGTH_SHORT).show()
                           navController.navigate("teacher")
                       } else {
                          if(save == false && errorMessage == true){
                              println("\nalgo deu errado $er \n")
                              Toast.makeText(context, "algo deu errado: $er" , Toast.LENGTH_LONG).show()
                          } else Toast.makeText(context, "tente novamente... $er" , Toast.LENGTH_SHORT).show()
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
    var save by remember {
        mutableStateOf(false)
    }

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

    var i = 0

    var er = ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "entrar no app prof")
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
                            er = "preencha todos os campos!"
                        } else if(userState.isNotEmpty() && password.value.isNotEmpty()){
                            var pass = password.value.toString()
                            teacherRepository.verifyTeacherLogin(userState, pass)
                            if(auth.currentUser != null) save = true
                        }
                    }

                    //mostrar mensagem usando o escopo do app -> context Main
                    scope.launch(Dispatchers.Main){
                        println(" count $i -> ${auth.currentUser}")
                        if(save  && (auth.currentUser!!.email.toString() == userState)){
                            println("\n teacher login feito com sucesso, save = $save \n")
                            Toast.makeText(context, "tudo ok", Toast.LENGTH_SHORT).show()
                            navController.navigate("teacher")
                        } else {
                            println("\nalgo deu errado, save = false $save , $er \n")
                            Toast.makeText(context, "algo deu errado ao fazer login, $er tente novamente ..." , Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }
}
