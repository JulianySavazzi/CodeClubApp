package com.example.codeclubapp.view

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.codeclubapp.model.Feed
import com.example.codeclubapp.model.Poll
import com.example.codeclubapp.model.Team
import com.example.codeclubapp.repository.FeedRepository
import com.example.codeclubapp.repository.PollRepository
import com.example.codeclubapp.repository.TeamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//EXAMPLE FOR DATETIME
/*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val dateTime = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a"))

    println(dateTime) // 01 de janeiro de 2017, 22:27:41
}
 */

@RequiresApi(Build.VERSION_CODES.O)
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

    val teamRepository = TeamRepository()

    val feedRepository = FeedRepository()

    val model = Poll()

    val feedModel = Feed()

    var endPoll: Boolean by remember {
        mutableStateOf(false)
    }

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var codeVal: MutableList<Long> = mutableListOf()

    var teamsForVotes: MutableList<Team> = mutableListOf()

    for (element in teamRepository.returnTeam()){
        teamsForVotes.add(element)
        print("lista de equipes: $element")
    }

    val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a"))

    var verifyStatusPoll = repository.verifyStatusPoll().endPoll

    var existPoll = repository.verifyStatusPoll()

    var newPoll = false

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


                    //verificações usando coroutines scope -> iniciar votacao
                    scope.launch(Dispatchers.IO){
                        //verificar se não tem nenhuma outra votação acontecendo
                        //se tiver, nao vai deixar criar uma nova antes da atual ser encerrada
                        if(verifyStatusPoll || existPoll.id != model.id){
                            //cria documento da votação -> dados que aparecerao na poll_screen
                            //inicia com a lista de codigos de validacao vazia, mostra todas as equipes cadastradas como opcoes de votacao
                            print("*** TENTANDO PREENCHER A LISTA DE EQUIPES ***")
                            repository.savePoll(model.id, /*model.codeVal*/ codeVal, model.qtdTotalVotes, /*model.teamsForVotes*/ teamsForVotes, endPoll)
                            feedRepository.saveFeed(
                                feedModel.id, "votação iniciada em $dateTime", "a votação está acontecendo, quando terminar, vamos publicar o resultado!"
                            )
                            print("*** VOTAÇÃO INICIADA ***")
                            newPoll = true
                        } else newPoll = false

                    }


                    scope.launch(Dispatchers.Main){
                            if(newPoll){
                                navController.navigate("teacher")
                                Toast.makeText(context, "votação iniciada!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "já temos uma votação acontecendo, espere ela terminar para iniciar outra.", Toast.LENGTH_SHORT).show()
                            }
                        }
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
                    //colocar data e hora no titulo da publicacao
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
                        //MyListPolls(position = position, listItem = pollList)
                    }
                }

            }
        }
        MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
    }

}

//mostrar as votações cadastradas
@Composable
fun MyListPolls(
    position: Int,
    listItem: MutableList<Poll>
    //context: Context,
    //navController: NavController
){
    //ligar a view com a model
    //var id: Int = identifier++,
    //var codeVal: MutableList<Long> = listCodeVal,
    //var qtdTotalVotes: Int = qtdTotal,
    //var teamsForVotes: MutableList<Team> = listTeams,
    //var endPoll: Boolean? = null,
    val idPoll = listItem[position].id
    val codValPoll = listItem[position].codeVal
    val qtdVotesPoll = listItem[position].qtdTotalVotes
    val teamsPoll = listItem[position].teamsForVotes
    val statusPoll = listItem[position].endPoll

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
                txtId,
                txtCodVal,
                txtQtdVotes,
                txtTeams,
                txtStatus
            ) = createRefs()

            Text(
                text = "id da votação: $idPoll",
                modifier = Modifier.constrainAs(txtId) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                text = "códigos de validação: ${codValPoll!![0]} ...",
                modifier = Modifier.constrainAs(txtCodVal) {
                    top.linkTo(txtId.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                text = "quantidade de votos: $qtdVotesPoll ...",
                modifier = Modifier.constrainAs(txtQtdVotes) {
                    top.linkTo(txtCodVal.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                text = "equipes elegíveis: ${teamsPoll!![0]} ...",
                modifier = Modifier.constrainAs(txtTeams) {
                    top.linkTo(txtQtdVotes.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                text = "status da votação: $statusPoll ...",
                modifier = Modifier.constrainAs(txtStatus) {
                    top.linkTo(txtTeams.bottom, margin = 15.dp)
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