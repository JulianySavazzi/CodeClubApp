package com.example.codeclubapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.model.LogPoll
import com.example.codeclubapp.model.NotificationFeed
import com.example.codeclubapp.repository.FeedRepository
import com.example.codeclubapp.repository.PollRepository

@Composable
fun Notifications(navController: NavController){
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    val feedRepository = FeedRepository()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "notificações")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .padding(10.dp)
                //.verticalScroll(rememberScrollState()) //barra de rolagem
        ) {
            //Rows -> corpo do app
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "veja todos os recados do(a) prof",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )

            }

            //mostrar mensagens
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ){
                //corpo do log
                val messagesList: MutableList<NotificationFeed> = feedRepository.getMessage().collectAsState(
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
                    itemsIndexed(messagesList){
                            position, _ -> MyListMessages(position = position, listItem = messagesList)
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
            MyAppBarBottom(
                navController = navController,
                loginTeacher = loginTeacher,
                loginStudent = loginStudent
            )
        }
    }

}

//show list messages
@Composable
fun MyListMessages(
    position: Int,
    listItem: MutableList<NotificationFeed>
){
    //val context: Context = LocalContext.current
    //ligar a view com a model
    val contentMessage = listItem[position].content

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
            // Create references for the composables to constraint
            val(
                txtTitle,
                txtDescription
            ) = createRefs()

            Text(
                text = contentMessage.toString(),
                modifier = Modifier.constrainAs(txtTitle) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )


        }
    }
    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
}



/*
//as notificações que o prof recebe são diferentes das recebidas pelos alunos
@Composable
fun NotificationsTeacher(navController: NavController){
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val pollRepository = PollRepository()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "notificações")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .padding(10.dp)
                //.verticalScroll(rememberScrollState()) //barra de rolagem
        ) {
            //Rows -> corpo do app
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(56.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "veja aqui todos os logs das votações ",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )

            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ){
                //corpo do log
                val logsList: MutableList<LogPoll> = pollRepository.getLogs().collectAsState(
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
                    itemsIndexed(logsList){
                            position, _ -> MyListLog(position = position, listItem = logsList)
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
            MyAppBarBottom(
                navController = navController,
                loginTeacher = loginTeacher,
                loginStudent = loginStudent
            )
        }
    }
}

 */

