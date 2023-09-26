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
import com.example.codeclubapp.components.MyButton
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.model.Feed
import com.example.codeclubapp.repository.FeedRepository
import com.example.codeclubapp.repository.TeacherRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//cadastrar noticias
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageFeed(navController: NavController){

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
    var titleState by remember {
        mutableStateOf("")
    }

    var contentState by remember {
        mutableStateOf("")
    }

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    //iniciar repositorio para salvar os dados no bd
    val feedRepository = FeedRepository()

    val model = Feed()

    //se salvou ou nao
    var save = false

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "publicações")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "nova publicação: ",
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
                value = titleState,
                onValueChange = {
                                titleState = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = "título",
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
            MyTextBoxInput(
                value = contentState,
                onValueChange = {
                                contentState = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = "conteúdo",
                maxLines = 10
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyLoginButton(
                text = "salvar publicação",
                modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                onClick = {

                    //verificações usando coroutines scope
                    scope.launch(Dispatchers.IO){
                        //verificar o estado dos campos
                        if(titleState.isEmpty() || contentState.isEmpty()){
                            save = false
                        } else if(titleState.isNotEmpty() && contentState.isNotEmpty()){
                            save = true
                            feedRepository.saveFeed(model.id, titleState, contentState)
                            if(feedRepository.getFeed() != null){
                                println("feed is not null")
                            } else {
                                save = false
                                print("feed is null")
                            }
                        }
                    }

                    //mostrar mensagem usando o escopo do app -> context Main
                    scope.launch(Dispatchers.Main){
                        if(save == true){
                            println("\nsalvo com sucesso \n")
                            titleState = ""
                            contentState = ""
                            // mostra publicacao -> feed_teacher
                            Toast.makeText(context, "salvo com sucesso ", Toast.LENGTH_SHORT).show()
                            navController.navigate("feed_teacher")
                        } else {
                            println("\nalgo deu errado \n")
                            Toast.makeText(context, "algo deu errado, preencha todos os campos!" , Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            )
        }
        /*
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
                text = "alterar publicação: ",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp

            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            //trocar botao por caixa de seleção
            MyButton(text = "selecionar publicação", route = "manageFeed", navController = navController, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                onValueChange = {
                    loginStudent
                    loginTeacher
                })
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyButton(text = "editar publicação", route = "manageFeed", navController = navController, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                onValueChange = {
                    loginStudent
                    loginTeacher
                })
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyButton(text = "excluir publicação", route = "manageFeed", navController = navController, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                onValueChange = {
                    loginStudent
                    loginTeacher
                })
        }
         */
        MyAppBarBottom(navController = navController, loginStudent=loginStudent, loginTeacher=loginTeacher)
    }
}