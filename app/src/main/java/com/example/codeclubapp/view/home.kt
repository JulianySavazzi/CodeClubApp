package com.example.codeclubapp.view

import android.app.AlertDialog
import android.content.res.Resources.Theme
import android.text.Html.ImageGetter
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Horizontal
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codeclubapp.ui.theme.RedCode
import com.example.codeclubapp.ui.theme.WHITE
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.content.ContextCompat.getSystemService
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.components.MyButton
import com.example.codeclubapp.components.MyCodeClubImage
import com.example.codeclubapp.components.MyLoginButton
import com.example.codeclubapp.model.Poll
import com.example.codeclubapp.repository.PollRepository
import com.example.codeclubapp.ui.theme.BLACK
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import okhttp3.internal.wait

//tela inicial do app
@ExperimentalMaterial3Api
@Composable
fun Home(navController: NavController){
    //utilizar firebase auth
    val auth = FirebaseAuth.getInstance()

    //coroutines trabalham com threads
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val pollRepository = PollRepository()

    //val existPoll = pollRepository.verifyPoll()

    //val listPoll = pollRepository.returnOnPoll()

    var listPollOn: MutableList<Poll> = mutableListOf()

    var existPoll = false

    var error = false

    fun pollDialog(){
        //aviso
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("NÃO É POSSÍVEL VOTAR!")
            .setMessage("nenhuma votação está disponível no momento, aguarde...")
            .setPositiveButton("DESISTIR"){
                    _, _, ->

                scope.launch(Dispatchers.Main){
                    //navegar para a pagina inicial
                    if(auth.currentUser != null) auth.signOut()
                    navController.navigate("home")
                    Toast.makeText(context, "nenhuma votação está disponível", Toast.LENGTH_SHORT).show()
                }
            }.setNegativeButton("[fechar aviso]"){
                _, _, ->
            }.show()
    }

    fun tryRedirect(){
        //verificações do login usando coroutines scope
        scope.launch(Dispatchers.IO){
            //verificar codigo de autenticacao na poll screen
            auth.signInAnonymously()
        }
        //escopo do app -> context Main
        scope.launch(Dispatchers.Main){
            //se existir uma votação ir para a tela de votacao
            //se nao, exibir um alert dizendo que nao tem votacoes disponiveis
            if(existPoll == true ){
                //if(listPoll.isNotEmpty()){
                if(auth.currentUser != null) auth.currentUser!!.isAnonymous.wait()
                println("\n login anonimo, existPoll = $existPoll, tentando ir para poll... \n")
                navController.navigate("poll")
                Toast.makeText(context, "abrindo votação...", Toast.LENGTH_SHORT).show()
            } else {
                //existPoll = false
                if(auth.currentUser != null) auth.signOut()
                navController.navigate("home")
                pollDialog()
                println(" pollDialog -> existPoll = $existPoll \n")
            }

        }
    }

    fun verifyPoll(){
        val refPoll = FirebaseFirestore.getInstance().collection("poll")
        //val query = refPoll.whereEqualTo("endPoll", true)
        //val queryNull = refPoll.whereEqualTo("endPoll", null)
        val queryPollOn = refPoll.whereEqualTo("endPoll", false)

        println(" Home: start verify poll -> init existPoll = $existPoll ")

        /*
        //se a votacao foi encerrada
        query!!.addSnapshotListener { snapshot, e ->
            //se retornou um erro
            if (e != null) error = true
            else {
                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    query.get().addOnCompleteListener {
                            querySnapshot ->
                        if(querySnapshot.isSuccessful){
                            //se a votacao foi encerrada
                            existPoll = false
                            println(" votação foi encerrada: $existPoll ")
                            return@addOnCompleteListener
                        }

                    }
                }

            }
        }

        //se a votacao nao foi iniciada -> null
        queryNull!!.addSnapshotListener { snapshot, e ->
            //se retornou um erro
            if (e != null) error = true
            else {
                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    queryNull.get().addOnCompleteListener{
                            querySnapshot ->
                        if(querySnapshot.isSuccessful){
                            //se nenhuma votacao foi iniciada
                            existPoll = false
                            println(" nenhuma votação foi iniciada: $existPoll ")
                            return@addOnCompleteListener
                        }
                    }
                }
            }
        }
         */

        //se existe uma votacao em andamento
        queryPollOn!!.addSnapshotListener { snapshot, e ->
            //se retornou um erro
            if (e != null) error = true
            else {
                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    queryPollOn.get().addOnCompleteListener {
                            querySnapshot ->
                        if(querySnapshot.isSuccessful){

                            for(document in querySnapshot.result){
                                //se a colecao existe e tem documentos
                                //vamos recuperar cada documento e adicionar no nosso objeto da model
                                val poll = document.toObject(com.example.codeclubapp.model.Poll::class.java)
                                listPollOn.add(poll)
                            }

                            existPoll = true
                            println(" votação está em andamento: $existPoll ")
                            //se existe uma votacao em andamento -> abrir tela de votacao
                            tryRedirect()
                            return@addOnCompleteListener
                        } else {
                            tryRedirect()
                        }
                    }
                }
            }
        }

        println("\n Home: end verify -> existPoll = $existPoll ")
        if(existPoll == false) tryRedirect()

    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MyAppBarTop(title = "code club")
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(56.dp, 56.dp, 56.dp, 0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                text = "bem vindo(a) ao code club app!",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold

            )

        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(56.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyCodeClubImage()
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){

            MyLoginButton(
                text = "votar no game challenge",
                //route = "poll",
                //navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                //onValueChange = {}
                onClick = {
                    print("\n home: verificando votação -> votação disponível: $existPoll ")
                    verifyPoll()
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
                MyButton(
                    text = "sou aluno(a)",
                    route = "user_student",
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    onValueChange = { loginStudent.value = true }
                )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            MyButton(
                text = "sou professor(a)",
                route = "login",
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                onValueChange = { loginTeacher.value = true }
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
           // MyAppBarBottom()
        }
    }
    //Text(text = "bem vindo ao code club!")
}


