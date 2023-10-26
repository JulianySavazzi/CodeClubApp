package com.example.codeclubapp.view

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.service.controls.ControlsProviderService
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.components.MyTextBoxInput
import com.example.codeclubapp.model.LogPoll
import com.example.codeclubapp.model.Poll
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import com.example.codeclubapp.repository.PollRepository
import com.example.codeclubapp.repository.TeamRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Poll(navController: NavController){
    //o usuario nao precisa estar logado, so precisa ter o codigo de autenticacao
    //entao nao sera nem aluno nem professor
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val pollRepository = PollRepository()
    val teamRepository = TeamRepository()

    //adicionar equipe selecionada nessa lista para salvar no banco -> contabilizar as votações
    val myTeams: MutableList<Team> = mutableListOf()

    //var selectedTeam: MutableList<Team> = mutableListOf()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var myPoll = Poll()
    var myLog = LogPoll()

    //criar estado para as caixas de texto:
    var codigoState by remember {
        //iniciar como uma string vazia
        mutableStateOf("")
    }

    //var idLogPoll = LogPoll().id

    val refPoll = FirebaseFirestore.getInstance().collection("poll")
    val refLog = FirebaseFirestore.getInstance().collection("log")
    //se a votacao nao foi encerrada e o codigo digitado esta cadastrado na votacao
    //val query = refPoll.whereEqualTo("endPoll", false).whereEqualTo("codeVal", listOfNotNull(codigoState))
    val query = refPoll.whereEqualTo("endPoll", false)
    //se o codigo foi cadastrado na votacao
    val queryLogCode = refLog.whereEqualTo("name", codigoState)
    //se o codigo ja foi usado
    val queryLogInvalidCode = refLog.whereEqualTo("description", codigoState)

    var error = false

    var existCode = false
    var saveVoteTeam = false

    //adicionar os codigos salvos para a votacao
    var codesList: MutableList<Long> = mutableListOf()
    var codesLogList: MutableList<String> = mutableListOf()
    var codesLogInvList: MutableList<String> = mutableListOf()

    fun getIdPoll(id: Int):Int {
        val totalVotesPoll = pollRepository.getPollById(id).qtdTotalVotes
        var myVotes: Int = totalVotesPoll + 1
        if(saveVoteTeam == true) pollRepository.updateVotesPoll(id, myVotes, myTeams)
        println(" * POLL ID $id updating total votes: $totalVotesPoll -> $myVotes * ")
        return id
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            //.verticalScroll(rememberScrollState()) //barra de rolagem
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "votação")
        //Rows -> corpo do app
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 20.dp, 10.dp, 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "digite o código de validação:",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
        }
        Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp, 0.dp, 5.dp, 0.dp,),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                //inserir codigo de validacao
                MyTextBoxInput(
                    //codigo
                    value = codigoState,
                    onValueChange = {
                        codigoState = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    label = "codigo",
                    maxLines = 1
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "escolha alguém para votar:",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    //.height(122.dp)
                    .fillMaxHeight(0.90f)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight(1f)
                        .padding(5.dp)
                    //.background(MaterialTheme.colorScheme.tertiary)
                ) {
                    //preencher a lista
                    val teamstList: MutableList<Team> = teamRepository.getTeam().collectAsState(
                        //se o estado da lista for vazio vai retornar uma mutableListOf
                        //se a lista tiver preenchida vai retornar os valores dos documentos
                        mutableListOf()
                    ).value

                    //componente de listagem
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        itemsIndexed(teamstList) { position, _ ->
                            VotesTeam(
                                position = position,
                                listItem = teamstList,
                                selectedItem = myTeams,
                                context = context,
                                navController = navController
                            )
                        }
                    }
                }
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                /*
                MyButton(
                    text = "", //nome do projeto
                    route = "home", //voto concluido
                    navController = navController,
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    onValueChange = {
                        loginStudent
                        loginTeacher
                    }
                )
                 */
                MyLoginButton(
                    text = "votar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                    onClick = {
                        //verificar codigo de votacao -> usar logs (codigo existe -> log descricao; dodigo ja foi usado -> log nome)
                        //confirmar voto em alert -> salvar voto
                        //logout

                        if(myTeams.isNotEmpty()){
                        //se uma equipe foi selecionada

                            var nameTeam: String = ""

                            var i: Int = myTeams!!.size

                            for (i in myTeams.indices){
                                nameTeam = myTeams[i].name.toString()
                            }

                            //atualizar valor de votos da equipe selecionada
                            val votes: Int = teamRepository.getTeamByName(nameTeam).vote
                            var myVote: Int = (votes + 1)

                            query!!.addSnapshotListener { snapshot, e ->
                                if (e != null) error = true
                                else {
                                    error = false
                                    if (snapshot != null && snapshot.documents.isNotEmpty()) {
                                        //se encontrou um documento que atende a query -> votacao nao foi encerrada
                                        query.get().addOnCompleteListener { querySnapshot ->
                                            if(querySnapshot.isSuccessful){
                                                for(document in querySnapshot.result){
                                                    //var i = 0
                                                    myPoll = document.toObject(Poll::class.java)
                                                    //se o codigo existe, vamos adicionar ele na lista de codigos
                                                    for (element in myPoll.codeVal!!){
                                                        codesList.add(element)
                                                        println(element.toString())
                                                    }
                                                    getIdPoll(myPoll.id)
                                                    Log.d(TAG, " id poll: ${myPoll.id} - this codes: ${listOf(codesList.toString())} ")
                                                    return@addOnCompleteListener
                                                }
                                            } else {
                                                Log.d(TAG, " this code is not found ")
                                            }
                                        }
                                    }

                                }
                            }

                            //se o codigo foi adicionado a votacao
                            queryLogCode.addSnapshotListener { snapshot, e ->
                                if (e != null) error = true
                                else {
                                    error = false
                                    if (snapshot != null && snapshot.documents.isNotEmpty()) {
                                        //se encontrou um documento que atende a query -> votacao nao foi encerrada
                                        queryLogCode.get().addOnCompleteListener { querySnapshot ->
                                            if(querySnapshot.isSuccessful){
                                                for(document in querySnapshot.result){
                                                    //var i = 0
                                                    myLog = document.toObject(LogPoll::class.java)
                                                    //se o codigo existe, vamos adicionar ele na lista de codigos
                                                    //codesList.add(i, myPoll.codeVal!![i])
                                                    if(myLog.name != null){
                                                        codesLogList.add(myLog.name!!)
                                                        println(codesLogList.toString())
                                                    }
                                                    //i++
                                                    Log.d(TAG, " this codes: ${listOf(codesLogList.toString())} ")
                                                    return@addOnCompleteListener
                                                }
                                            } else {
                                                Log.d(TAG, " this code is not found ")
                                            }
                                        }
                                    }

                                }
                            }

                            //se o codigo ja foi usado
                            queryLogInvalidCode.addSnapshotListener { snapshot, e ->
                                if (e != null) error = true
                                else {
                                    error = false
                                    if (snapshot != null && snapshot.documents.isNotEmpty()) {
                                        //se encontrou um documento que atende a query -> votacao nao foi encerrada
                                        queryLogInvalidCode.get().addOnCompleteListener { querySnapshot ->
                                            if(querySnapshot.isSuccessful){
                                                for(document in querySnapshot.result){
                                                    //var i = 0
                                                    myLog = document.toObject(LogPoll::class.java)
                                                    //se o codigo existe, vamos adicionar ele na lista de codigos
                                                    //codesList.add(i, myPoll.codeVal!![i])
                                                    if(myLog.description != null){
                                                        codesLogInvList.add(myLog.description!!)
                                                        println(codesLogInvList.toString())
                                                        pollRepository.saveLog(myLog.id, "código: ${myLog.description} já foi utilizado! ", "${myLog.description}")
                                                    }
                                                    //i++
                                                    Log.d(TAG, " this codes: ${listOf(codesLogInvList.toString())} in invalid codes list")
                                                    return@addOnCompleteListener
                                                }
                                            } else {
                                                Log.d(TAG, " this code is not found ")
                                            }
                                        }
                                    }

                                }
                            }

                            println(" *** team selected: $nameTeam - votes: $votes - updated votes: $myVote *** ")
                            //println(" ID POLL: ${myPollId.toString()}")
                            println(" codes = ${codesList.toString()} || ${codesLogList.toString()} ")
                            println(" invalid codes = ${codesLogInvList.toString()} ")


                            if(codesList.isNotEmpty() || codesLogList.isNotEmpty()){
                                existCode = true
                                if(codigoState.isNotEmpty()){
                                    if(codesList.toString().contains(codigoState) || codesLogList.toString().contains(codigoState) && existCode == true) {
                                        //se o codigo for valido ele sera utilizado
                                        //se o codigo for utilizado, gerar um log dizendo que ele nao pode ser usado novamente nessa votacao
                                        //adicionar voto para equipe votada e atualizar o total de votos de cada equipe e o total de votos da votacao
                                        println(" existe code = $existCode - input code = $codigoState ")
                                        if(!codesLogInvList.toString().contains(codigoState)){
                                            //salvar voto
                                            teamRepository.updateVoteTeamByName(nameTeam, myVote)
                                            //pollRepository.updateVotesPoll()
                                            pollRepository.saveLog(myLog.id, "código: $codigoState já foi utilizado! ", "$codigoState")
                                            println(" user ${Firebase.auth.currentUser} - existCode = $existCode : try update votes and save log... ")
                                            Firebase.auth.signOut()
                                            if(Firebase.auth.currentUser == null){
                                                saveVoteTeam = true
                                                Toast.makeText(context, " OK: codigo -> ${codesList.toString()} || ${codesLogList.toString()} == ${codigoState.toString()} \n voto salvo com sucesso! " , Toast.LENGTH_SHORT).show()
                                                println(" sing out poll 1 - update vote and save log ")
                                                navController.navigate("home")
                                            } else {
                                                Firebase.auth.signOut()
                                                Toast.makeText(context, " AVISO: codigo -> ${codesList.toString()} || ${codesLogList.toString()} == ${codigoState.toString()} \n não conseguimos salvar seu voto! " , Toast.LENGTH_SHORT).show()
                                                println(" sing out poll 2 - fail ")
                                                navController.navigate("home")
                                            }

                                        }else {
                                            Toast.makeText(context, " ERRO: codigo inválido -> ${codesLogInvList.toString()} == ${codigoState.toString()} \n esse código já foi utilizado! " , Toast.LENGTH_SHORT).show()
                                        }
                                    } else Toast.makeText(context, " algo deu errado: codigo -> ${codesList.toString()} || ${codesLogList.toString()} != ${codigoState.toString()} " , Toast.LENGTH_SHORT).show()

                                } else Toast.makeText(context, "o código de validação não pode ser vazio!" , Toast.LENGTH_SHORT).show()
                            } else {
                                existCode = false
                                println(" existCode = $existCode ")
                            }
                        }else{
                            Toast.makeText(context, "selecione uma equipe para votar!" , Toast.LENGTH_SHORT).show()
                        }

                    })

            }
        }

}

@Composable
fun VotesTeam(
    position: Int,
    listItem: MutableList<Team>,
    context: Context,
    navController: NavController,
    selectedItem: MutableList<Team>
) {

    var selectedOption: Boolean by remember {
        mutableStateOf(false)
    }

    //ligar a view com a model
    val titleTeam = listItem[position].name
    //val projectsTeam = listItem[position].projects

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current



    //enviar equipe selecionada para a funcao de salvar voto
    Divider(
        thickness = 5.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
    Card(
        modifier = Modifier.padding(5.dp
        )
    ){
        ConstraintLayout(
            modifier = Modifier.padding(5.dp)
        ) {
            // Create references for the composables to constrain
            val (
                txtTitle,
                //txtProjects,
                check
            ) = createRefs()

            RadioButton(
                selected = selectedOption,
                onClick = {
                    selectedOption = true
                    //adicionar item selecionado na lista
                    if(selectedOption && selectedItem.size == 0){
                        selectedItem.add(listItem[position])
                        //passar codigos validos
                        print("equipe selecionada: $selectedItem")
                    } else {
                        selectedOption = false
                        print("já tem uma equipe selecionada: $selectedItem")
                        //usar navigator para atualizar pagina
                        navController.navigate("poll")
                    }
                          },
                modifier = Modifier
                    .constrainAs(check) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(parent.start, margin = 10.dp)
                    }

                )

            Text(
                text = "$titleTeam",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                modifier = Modifier
                    .constrainAs(txtTitle) {
                        top.linkTo(parent.top, margin = 10.dp)
                        start.linkTo(check.end, margin = 10.dp)
                    }
                    .padding(start = 16.dp)
            )
        }
    }
    Divider(
        thickness = 10.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )

}