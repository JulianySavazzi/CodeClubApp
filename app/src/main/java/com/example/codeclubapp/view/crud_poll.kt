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
import com.example.codeclubapp.model.LogPoll
import com.example.codeclubapp.model.Poll
import com.example.codeclubapp.model.Team
import com.example.codeclubapp.repository.FeedRepository
import com.example.codeclubapp.repository.PollRepository
import com.example.codeclubapp.repository.TeamRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random.Default.nextLong

//fun nextLong(until: Long): Long

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

    var click by remember{
        mutableStateOf(false)
    }

    val idLogPoll = LogPoll().id

    fun startPoll(){
        //add methodo para verificar se existe alguma votacao encerrada antes de iniciar uma nova
        //se existir uma votacao encerrada -> excluir ela antes de iniciar a nova votacao
        //todos os dados dela serao salvos no log

        val refPoll = FirebaseFirestore.getInstance().collection("poll")
        val query = refPoll.whereEqualTo("endPoll", false)
        //val query2 = refPoll.whereEqualTo("endPoll", true)

        query!!.addSnapshotListener { snapshot, e ->
            if (e != null) error = true
            else {
                error = false
                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    newPoll = false
                } else {

                    repository.savePoll(
                        model.id, /*model.codeVal*/
                        codeVal,
                        model.qtdTotalVotes, /*model.teamsForVotes*/
                        teamsVoted,
                        false
                    )
                    //repository.verifyStatusPoll(model.id, codeVal, model.qtdTotalVotes, teamsVoted, endPoll)
                    feedRepository.saveFeed(
                        feedModel.id,
                        "votação iniciada em $dateTime",
                        "a votação está acontecendo, quando terminar, vamos publicar o resultado!"
                    )
                    repository.saveLog(idLogPoll, "votação ${model.id} iniciada ", "votação iniciada em $dateTime, quantidade de votos: ${model.qtdTotalVotes}, integrantes: ${teamsVoted.size}")

                    /*
                    query2!!.addSnapshotListener { snapshot, e ->
                        if(e != null) error = true
                        else {
                            error = false
                            if(snapshot != null && snapshot.documents.isNotEmpty()){
                                query2!!.get().addOnCompleteListener{
                                        querySnapshot ->
                                    if(querySnapshot.isSuccessful){
                                        //se existem votacoes finalizadas
                                        for(document in querySnapshot.result){
                                            //se a colecao existe e tem documentos
                                            //vamos recuperar cada documento e adicionar no nosso objeto da model
                                            val poll = document.toObject(Poll::class.java)
                                            val idPollEnd = poll.id
                                            repository.saveLog(idLogPoll, "votação $idPollEnd finalizada e excluída", "votação excluída em $dateTime, quantidade de votos: ${model.qtdTotalVotes}, integrantes: ${teamsVoted.size}")
                                            repository.deletePoll(idPollEnd)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    */

                }


                print("*** VOTAÇÃO INICIADA ***")
                newPoll = true

            }
        }

    }

    /*
    //add dialog para iniciar votacao
    fun startPollDialog(){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("INICIAR VOTAÇÃO")
            .setMessage("tem certeza que quer iniciar uma nova VOTAÇÃO ?")
            .setPositiveButton("Sim"){
                    _, _, ->

                scope.launch(Dispatchers.IO){
                    //verificar se não tem nenhuma outra votação acontecendo
                    //se tiver, nao vai deixar criar uma nova antes da atual ser encerrada
                    //if(this.isActive) {
                    //}
                    startPoll()
                }
            }
            .setNegativeButton("Não"){
                    _, _, ->
            }
            .show()
    }
     */

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "gerenciar votação")
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
                    //click = true
                    //println(" verifyStatusPoll = $verifyStatusPoll; existPoll.id = ${existPoll.id}; model.id = ${model.id}; empty? ${getPoll.isEmpty()}")
                    //verificações usando coroutines scope -> iniciar votacao
                    //startPollDialog()
                    scope.launch(Dispatchers.IO){
                        //verificar se não tem nenhuma outra votação acontecendo
                        //se tiver, nao vai deixar criar uma nova antes da atual ser encerrada
                        //if(this.isActive) {
                        //}
                        startPoll()
                    }

                    scope.launch(Dispatchers.Main){
                        //if(this.isActive){
                        if (error) Toast.makeText(context, "aconteceu um problema!", Toast.LENGTH_SHORT).show()
                        if(newPoll == true){
                            navController.navigate("teacher")
                            Toast.makeText(context, "votação iniciada!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "já temos uma votação acontecendo, espere ela terminar para iniciar outra.", Toast.LENGTH_SHORT).show()
                            navController.navigate("managePolls")
                        }
                        //click = false
                        //}
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

        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 30.dp, 20.dp, 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){

            MyLoginButton(
                text = "exibir log das votações",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                onClick = {
                    navController.navigate("logsPoll")
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

/*
* mostrar as votações cadastradas:
* listar todas as votações que estao no bd
 */

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
    val idLogPoll = LogPoll().id

    val repository = PollRepository()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val scopeCodVal = rememberCoroutineScope()

    val feedRepository = FeedRepository()
    val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a"))
    val feedModel = Feed()

    var codeList: MutableList<Long> = mutableListOf()

    var updatePoll = false

    var endPoll = false

    var error = false

    var saveCode by remember {
        mutableStateOf(false)
    }

    val refPoll = FirebaseFirestore.getInstance().collection("poll")
    val query = refPoll.whereEqualTo("endPoll", false).whereEqualTo("id", idPoll)

    /*
    var click by remember{
        mutableStateOf(false)
    }
     */

    //limitar codigo a numeros positivos de 6 digitos de 100000 até 999999
    val code = nextLong(100000, 999999)

    fun deleteDialog(){
        //deletar estudante
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("EXCLUIR VOTAÇÃO")
            .setMessage("tem certeza que quer excluir essa VOTAÇÃO ?")
            .setPositiveButton("Sim"){
                    _, _, ->
                repository.deletePoll(idPoll)
                repository.saveLog(idLogPoll, "votação $idPoll excluída ", "votação excluída em $dateTime, quantidade de votos: $qtdVotesPoll")

                scope.launch(Dispatchers.Main){
                    //remover equipe excluido da lista
                    listItem.removeAt(position)
                    //navegar para a pagina para atualizar a listagem
                    navController.navigate("teacher")
                    Toast.makeText(context, "votação excluída com sucesso!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Não"){
                    _, _, ->
            }
            .show()
    }

    fun updateStatusPoll(){
        query!!.addSnapshotListener { snapshot, e ->
            if (e != null) error = true
            else {
                error = false
                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    //se existe uma votação em andamento -> endPoll = false
                    query.get().addOnCompleteListener { querySnapshot ->
                        updatePoll = if(querySnapshot.isSuccessful) querySnapshot.isSuccessful
                        else false
                        print(" updatePoll = $updatePoll ")
                        if(updatePoll){
                            repository.updatePoll(idPoll, true)
                            feedRepository.saveFeed(
                                feedModel.id,
                                "votação finalizada em $dateTime",
                                "a votação foi encerrada!"
                            )
                            repository.saveLog(idLogPoll, "votação $idPoll encerrada ", "votação finalizada em $dateTime, quantidade de votos: $qtdVotesPoll")
                            endPoll = true
                            print("*** VOTAÇÃO FINALIZADA ***")
                            //repository.saveLog(idLogPoll, "votação $idPoll finalizada e excluída", "votação excluída em $dateTime, quantidade de votos: $qtdVotesPoll")
                            //repository.deletePoll(idPoll)
                        } else print("*** VOTAÇÃO NÃO PODE SER FINALIZADA ***")
                    }
                } else {
                    endPoll = false
                }
            }
        }
    }

    fun alertCode(){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("CÓDIGO PARA VALIDAR VOTAÇÃO!")
            .setMessage("copie esse código e insira na tela de votação para confirmar seu voto: $code")
            .setPositiveButton("OK"){
                    _, _, ->
                scope.launch(Dispatchers.Main){
                    //repository.saveLog(idLogPoll, "votação $idPoll -> novo codigo de validação foi adicionado em $dateTime: ", "$code")
                    Toast.makeText(context, "código salvo!", Toast.LENGTH_SHORT).show()
                    navController.navigate("teacher")
                }

            }.show()
    }

    fun saveCode(){
        refPoll.whereEqualTo("codVal", code).whereEqualTo("endPoll", false).whereEqualTo("id", idPoll)!!.addSnapshotListener {
                snapshot, e ->
                if (e != null) error = true
                else {
                    error = false
                    if (snapshot != null && snapshot.documents.isNotEmpty()) {
                        query.get().addOnCompleteListener { querySnapshot ->
                            //se existe um documento com essas caracteristicas
                            if(querySnapshot.isSuccessful) {
                                saveCode = false
                                println(" isSuccessful , saveCode = $saveCode")
                            }
                        }
                    } else {
                        codeList.add(code)
                        repository.updateCodValPoll(idPoll, codeList)
                        //gerar log com o codigo e a votacao
                        repository.saveLog(idLogPoll, "$code", "código $code criado com sucesso!")
                        alertCode()
                        saveCode = true
                        println(" bad snapshot1 saveCode = $saveCode")
                    }

                }
            }

        println(" fun saveCode = $saveCode ")
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
                    //click = true
                    //atualiza atributo endPoll do documento da votação para true
                    //contabiliza o resultado da votação e salva uma publicação com o resultado
                    //colocar data e hora no titulo da publicacao
                    updateStatusPoll()

                    println("\nupdatePoll = $updatePoll, endPoll = $endPoll")
                    scope.launch(Dispatchers.Main){
                        if (error) Toast.makeText(context, "aconteceu um problema!", Toast.LENGTH_SHORT).show()
                        if(updatePoll == true || endPoll == true){
                            navController.navigate("teacher")
                            repository.saveLog(idLogPoll, "votação $idPoll encerrada e excluída", "votação excluída em $dateTime, quantidade de votos: $qtdVotesPoll")
                            repository.deletePoll(idPoll)
                            Toast.makeText(context, "votação finalizada!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "não foi possível encerrar a votação.", Toast.LENGTH_SHORT).show()
                            navController.navigate("teacher")
                        }
                        //click = false
                    }

                    //scope.cancel()
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
                    //atualiza atributo codVal do documento da votação -> insere o codigo na lista
                    //esse codigo vai verificar se o usuario pode votar ou não
                    //o codigo so pode ser gerado se a votacao ja foi iniciada  e nao foi finalizada
                    println(" tentando cadastrar código... ")

                    saveCode()

                    println(" saveCode = $saveCode ")
                    scope.launch(Dispatchers.Main){
                        //atualizar codigo
                        //println(" scope main -> saveCode = $saveCode ")
                        if (error) Toast.makeText(context, "aconteceu um problema!", Toast.LENGTH_SHORT).show()
                    }

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