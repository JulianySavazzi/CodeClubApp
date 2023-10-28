package com.example.codeclubapp.view

import android.content.Intent
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
import com.example.codeclubapp.MainActivity
import com.example.codeclubapp.MyFirebaseMessagingService
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyButton
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.model.Feed
import com.example.codeclubapp.model.NotificationFeed
import com.example.codeclubapp.repository.FeedRepository
import com.example.codeclubapp.repository.TeacherRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import okhttp3.internal.wait

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

    val myFirebaseMessagingService = MyFirebaseMessagingService()
    val firebaseMessagingService = FirebaseMessagingService()

    //iniciar repositorio para salvar os dados no bd
    val feedRepository = FeedRepository()

    val model = Feed()
    val messageModel = NotificationFeed()

    //se salvou ou nao
    var save = false
    var isNull = false

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "publicações")
        //Rows -> corpo do app
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(10.dp)
        ){
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
            Divider(
                thickness = 56.dp,
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                color = MaterialTheme.colorScheme.background
            )
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
                    maxLines = 20
                )
            }
            Divider(
                thickness = 56.dp,
                modifier = Modifier.fillMaxWidth().fillMaxHeight(1f),
                color = MaterialTheme.colorScheme.background
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            MyLoginButton(
                text = "salvar publicação",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {

                //verificações usando coroutines scope
                scope.launch(Dispatchers.IO) {
                    //verificar o estado dos campos
                    if (titleState.isEmpty() || contentState.isEmpty()) {
                        save = false
                    } else if (titleState.isNotEmpty() && contentState.isNotEmpty()) {
                        feedRepository.saveFeed(model.id, titleState, contentState)
                        feedRepository.saveMessage(messageModel.id, "$titleState \n $contentState")
                        save = true
                        //fazer verificacao no dataSource
                        if (feedRepository.getFeedByName(titleState, contentState).id == model.id){
                            println("feed is not null")
                        } else {
                            save = false
                            isNull = true
                            print("feed is null")
                        }
                    }
                }

                //mostrar mensagem usando o escopo do app -> context Main
                scope.launch(Dispatchers.Main) {
                    if (save && !isNull) {
                        println("\nsalvo com sucesso \n")
                        titleState = ""
                        contentState = ""
                        //show notification
                        firebaseMessagingService.onMessageSent("")
                        myFirebaseMessagingService.generateNotification("nova publicação","${titleState} \n ${contentState}")
                        // mostra publicacao -> feed_teacher
                        Toast.makeText(context, "salvo com sucesso ", Toast.LENGTH_SHORT).show()
                        navController.navigate("feed_teacher")

                    } else {
                        println("\nalgo deu errado \n")
                        Toast.makeText(
                            context,
                            "algo deu errado, preencha todos os campos!",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("feed_teacher")
                    }
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            MyAppBarBottom(navController = navController, loginStudent=loginStudent, loginTeacher=loginTeacher)
        }
    }
}

