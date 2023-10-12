package com.example.codeclubapp.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import android.service.controls.ControlsProviderService
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Block
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
import com.google.firebase.firestore.FirebaseFirestore
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

    //val teamRepository = TeamRepository()

    val repository = PollRepository()

    val feedRepository = FeedRepository()

    val model = Poll()

    val feedModel = Feed()

    /*
    var endPoll: Boolean by remember {
        mutableStateOf(false)
    }
    */

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var codeVal: MutableList<Long> = mutableListOf()

    var teamsVoted: MutableList<Team> = mutableListOf()

    /*
    for (element in teamRepository.returnTeam()){
        teamsForVotes.add(element)
        print("lista de equipes: $element")
    }
     */

    val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a"))

    //val verifyStatusPoll = repository.verifyStatusPoll().endPoll

    //val verifyPoll = repository.verifyStatusPoll(model.id, codeVal, model.qtdTotalVotes, teamsVoted, endPoll)

    //val existPoll = repository.verifyStatusPoll()

    //val getPoll = repository.returnPoll()

    var newPoll = false

    var endPoll = false

    var error = false

    val refPoll = FirebaseFirestore.getInstance().collection("poll")
    val query = refPoll.whereEqualTo("endPoll", false)

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

                    //println(" verifyStatusPoll = $verifyStatusPoll; existPoll.id = ${existPoll.id}; model.id = ${model.id}; empty? ${getPoll.isEmpty()}")
                    //verificações usando coroutines scope -> iniciar votacao
                    scope.launch(Dispatchers.IO){
                        //verificar se não tem nenhuma outra votação acontecendo
                        //se tiver, nao vai deixar criar uma nova antes da atual ser encerrada
                        /*
                        if(verifyStatusPoll != false && existPoll.id != model.id || getPoll.isEmpty() && verifyStatusPoll == true && existPoll.id != model.id){
                            //cria documento da votação -> dados que aparecerao na poll_screen
                            //inicia com a lista de codigos de validacao vazia,e lista de equipes vazia -> adicionar as equipes na lista de acordo com as votacoes
                            //print("*** TENTANDO PREENCHER A LISTA DE EQUIPES ***")
                            repository.savePoll(model.id, /*model.codeVal*/ codeVal, model.qtdTotalVotes, /*model.teamsForVotes*/ teamsVoted, endPoll)
                            feedRepository.saveFeed(
                                feedModel.id, "votação iniciada em $dateTime", "a votação está acontecendo, quando terminar, vamos publicar o resultado!"
                            )
                            print("*** VOTAÇÃO INICIADA ***")
                            newPoll = true
                        } else newPoll = false
                         */
                        query!!.addSnapshotListener{ snapshot, e ->
                            if(e != null) error = true
                            else{
                                error = false
                                if(snapshot != null && snapshot.documents.isNotEmpty()){
                                    newPoll = false
                                } else {
                                    repository.savePoll(model.id, /*model.codeVal*/ codeVal, model.qtdTotalVotes, /*model.teamsForVotes*/ teamsVoted, false)
                                    //repository.verifyStatusPoll(model.id, codeVal, model.qtdTotalVotes, teamsVoted, endPoll)
                                    feedRepository.saveFeed(
                                        feedModel.id, "votação iniciada em $dateTime", "a votação está acontecendo, quando terminar, vamos publicar o resultado!"
                                    )
                                    print("*** VOTAÇÃO INICIADA ***")
                                    newPoll = true
                                }
                            }
                        }

                    }

                    scope.launch(Dispatchers.Main){
                            if (error) Toast.makeText(context, "aconteceu um problema!", Toast.LENGTH_SHORT).show()
                            if(newPoll == true){
                                navController.navigate("teacher")
                                Toast.makeText(context, "votação iniciada!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "já temos uma votação acontecendo, espere ela terminar para iniciar outra.", Toast.LENGTH_SHORT).show()
                                navController.navigate("managePolls")
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
            /*
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
                    scope.launch(Dispatchers.IO) {
                        query!!.addSnapshotListener{ snapshot, e ->
                            if(e != null) error = true
                            else{
                                error = false
                                if(snapshot != null && snapshot.documents.isNotEmpty()){
                                    //se existe uma votação em andamento
                                    query.get().addOnCompleteListener { querySnapshot ->
                                        if (querySnapshot.isSuccessful) {
                                            for (document in querySnapshot.result) {
                                                var thisPoll = document.toObject(Poll::class.java)
                                                if(thisPoll.endPoll == false && thisPoll.endPoll != null && thisPoll.endPoll != true){
                                                    Log.d(
                                                        ControlsProviderService.TAG,
                                                        "endPoll: ${thisPoll.endPoll}, this poll may be finished!"
                                                    )
                                                    repository.updatePoll(thisPoll.id, true)
                                                    break
                                                } else break
                                                return@addOnCompleteListener
                                            }
                                        }
                                    }
                                    feedRepository.saveFeed(
                                        feedModel.id, "votação finalizada em $dateTime", "a votação foi encerrada!"
                                    )
                                    endPoll = true
                                    print("*** VOTAÇÃO FINALIZADA ***")
                                } else {
                                    endPoll = false
                                }
                            }
                        }
                    }

                    scope.launch(Dispatchers.Main){
                        if (error) Toast.makeText(context, "aconteceu um problema!", Toast.LENGTH_SHORT).show()
                        if(endPoll == true){
                            navController.navigate("teacher")
                            Toast.makeText(context, "votação finalizada!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "não foi possível encerrar a votação.", Toast.LENGTH_SHORT).show()
                            navController.navigate("managePolls")
                        }
                    }

                }
            )
             */
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
                val   pollList: MutableList<Poll> = repository.getPoll().collectAsState(
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
                        MyListPolls(position = position, listItem = pollList, context = context, navController = navController)
                    }
                }

            }
        }
        MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
    }

}

//mostrar as votações cadastradas
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyListPolls(
    position: Int,
    listItem: MutableList<Poll>,
    context: Context,
    navController: NavController
){
    //ligar a view com a model
    //var id: Int = identifier++,
    //var codeVal: MutableList<Long> = listCodeVal,
    //var qtdTotalVotes: Int = qtdTotal,
    //var teamsForVotes: MutableList<Team> = listTeams,
    //var endPoll: Boolean? = null,
    val idPoll = listItem[position].id
    //val codValPoll = listItem[position].codeVal
    val qtdVotesPoll = listItem[position].qtdTotalVotes
    //val teamsPoll = listItem[position].teamsVoted
    val statusPoll = listItem[position].endPoll

    val repository = PollRepository()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()

    val feedRepository = FeedRepository()
    val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a"))
    val feedModel = Feed()

    var newPoll = false

    var endPoll = false

    var error = false

    val refPoll = FirebaseFirestore.getInstance().collection("poll")
    val query = refPoll.whereEqualTo("endPoll", false)

    fun deleteDialog(){
        //deletar estudante
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("EXCLUIR VOTAÇÃO")
            .setMessage("tem certeza que quer excluir essa VOTAÇÃO ?")
            .setPositiveButton("Sim"){
                    _, _, ->
                //funcao delete
                repository.deletePoll(idPoll)

                scope.launch(Dispatchers.Main){
                    //remover equipe excluido da lista
                    listItem.removeAt(position)
                    //navegar para a pagina para atualizar a listagem
                    navController.navigate("managePolls")
                    Toast.makeText(context, "votação excluída com sucesso!", Toast.LENGTH_SHORT).show()
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
        modifier = Modifier.padding(15.dp
        )
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(15.dp)
        ) {
            // Create references for the composables to constrain
            val(
                txtId,
                //txtCodVal,
                txtQtdVotes,
                //txtTeams,
                txtStatus,
                navBarItemDelete,
                navBarStop,
                navBarCod
            ) = createRefs()

            Text(
                text = "id da votação: $idPoll",
                modifier = Modifier.constrainAs(txtId) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            /*
            Text(
                text = "códigos de validação: ${codValPoll!![0]} ...",
                modifier = Modifier.constrainAs(txtCodVal) {
                    top.linkTo(txtId.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

             */

            Text(
                text = "quantidade de votos: $qtdVotesPoll ...",
                modifier = Modifier.constrainAs(txtQtdVotes) {
                    top.linkTo(txtId.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            /*
            Text(
                text = "equipes elegíveis: ${teamsPoll!![0]} ...",
                modifier = Modifier.constrainAs(txtTeams) {
                    top.linkTo(txtQtdVotes.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )
             */

            Text(
                text = "status da votação: encerrada -> $statusPoll ...",
                modifier = Modifier.constrainAs(txtStatus) {
                    top.linkTo(txtQtdVotes.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            IconButton(
                onClick = {
                    deleteDialog()
                },
                modifier = Modifier.constrainAs(navBarItemDelete) {
                    top.linkTo(txtStatus.bottom, margin = 15.dp)
                    start.linkTo(txtStatus.end, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
            ) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.icon_delete_24), contentDescription ="excluir")
            }

            //colocar o botão para encerrar a votação aqui
            IconButton(
                onClick = {
                    //atualiza atributo endPoll do documento da votação para true
                    //contabiliza o resultado da votação e salva uma publicação com o resultado
                    //colocar data e hora no titulo da publicacao
                    scope.launch(Dispatchers.IO) {
                        query!!.addSnapshotListener{ snapshot, e ->
                            if(e != null) error = true
                            else{
                                error = false
                                if(snapshot != null && snapshot.documents.isNotEmpty()){
                                    //se existe uma votação em andamento
                                    query.get().addOnCompleteListener { querySnapshot ->
                                        if (querySnapshot.isSuccessful) {
                                            for (document in querySnapshot.result) {
                                                var thisPoll = document.toObject(Poll::class.java)
                                                Log.d(
                                                    ControlsProviderService.TAG,
                                                    "endPoll: ${thisPoll.endPoll}|${idPoll}, this poll may be finished!"
                                                )
                                                repository.updatePoll(idPoll, true)
                                                break
                                                return@addOnCompleteListener
                                            }
                                        }
                                    }
                                    feedRepository.saveFeed(
                                        feedModel.id, "votação finalizada em $dateTime", "a votação foi encerrada!"
                                    )
                                    endPoll = true
                                    print("*** VOTAÇÃO FINALIZADA ***")
                                } else {
                                    endPoll = false
                                }
                            }
                        }
                    }

                    scope.launch(Dispatchers.Main){
                        if (error) Toast.makeText(context, "aconteceu um problema!", Toast.LENGTH_SHORT).show()
                        if(endPoll == true){
                            navController.navigate("teacher")
                            Toast.makeText(context, "votação finalizada!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "não foi possível encerrar a votação.", Toast.LENGTH_SHORT).show()
                            navController.navigate("managePolls")
                        }
                    }
                },
                modifier = Modifier.constrainAs(navBarStop) {
                    top.linkTo(txtStatus.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                    end.linkTo(navBarCod.start, margin = 15.dp)
                }
            ) {
                //Icons.Filled.Block
                //Text(text = "encerrar")
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_pause_circle_24), contentDescription ="encerrar")
            }

            //colocar o botão para gerar codigos da votação aqui
            IconButton(
                onClick = {

                },
                modifier = Modifier.constrainAs(navBarCod) {
                    top.linkTo(txtStatus.bottom, margin = 15.dp)
                    start.linkTo(navBarStop.end, margin = 15.dp)
                    end.linkTo(navBarItemDelete.start, margin = 15.dp)
                }
            ) {
                //Icons.Filled.AddCircleOutline
                //Text(text = "gerar código")
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_new_label_24), contentDescription ="adicionar código")
            }

        }
    }
    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )

}