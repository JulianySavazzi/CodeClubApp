package com.example.codeclubapp.view

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
import androidx.navigation.NavController
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.model.Poll
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.repository.PollRepository

@Composable
fun ManagePolls(navController: NavController){
    //apenas o usuario do tipo professor tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val repository = PollRepository()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "criar votação")
        //Rows -> corpo do app\
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "gerenciar votação: ",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp

            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            MyLoginButton(
                text = "iniciar votação",
                modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.secondary),
                onClick = {
                    //cria documento da votação -> dados que aparecerao na poll_screen
                    //inicia com a lista de codigos de validacao vazia, mostra todas as equipes cadastradas como opcoes de votacao
                }
            )

        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            MyLoginButton(
                text = "gerar código de validação",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                onClick = {
                    //atualiza atributo codVal do documento da votação -> insere o codigo na lista
                    //esse codigo vai verificar se o usuario pode votar ou não
                    //o codigo so pode ser gerado se a votacao ja foi iniciada
                }
            )

        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            MyLoginButton(
                text = "encerrar votação",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                onClick = {
                    //atualiza atributo endPoll do documento da votação para true
                    //contabiliza o resultado da votação e salva uma publicação com o resultado
                }
            )

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
                text = "votações cadastradas: ",
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
                val pollList: MutableList<Poll> = repository.getPoll().collectAsState(
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
                    itemsIndexed(pollList){
                            position, _ ->
                        //MyListPolls(position = position, listItem = pollList, context = context, navController = navController)
                    }
                }

            }
        }
        MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
    }

}