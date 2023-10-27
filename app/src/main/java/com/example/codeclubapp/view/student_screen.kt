package com.example.codeclubapp.view

import android.content.ContentValues
import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import com.example.codeclubapp.repository.ProjectRepository
import com.example.codeclubapp.repository.StudentRepository
import com.example.codeclubapp.repository.TeamRepository
import com.example.codeclubapp.ui.theme.GreenLightCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun Student(navController: NavController){
    //apenas o usuario do tipo aluno tem acesso a essa tela
    val loginTeacher = remember {
        //a senha fica invisiível
        mutableStateOf(false)
    }

    val loginStudent = remember {
        //a senha fica invisiível
        mutableStateOf(true)
    }

    //utilizar firebase auth
    val auth = FirebaseAuth.getInstance()

    val teamsRepo = TeamRepository()

    //val projectRepo = ProjectRepository()

    val repository = StudentRepository()

    var studentList: MutableList<Student> = mutableListOf()

    var membersTeam: MutableList<Team> = mutableListOf()

    var error = false

    //mostrar projetos e equipes
    var nameStudent = ""
    var nameProjects: MutableList<String> = mutableListOf()
    var nameTeams: MutableList<String> = mutableListOf()

    var i: Int = membersTeam!!.size

    //pegar nome das equipes e projetos que esse aluno participa
    val refTeam = FirebaseFirestore.getInstance().collection("team")
    val refStudent = FirebaseFirestore.getInstance().collection("student")

    val queryStudent = refStudent.whereEqualTo("email", auth.currentUser?.email).whereEqualTo("isStudent", true)

    /*
    for (i in studentList.indices){
        nameStudent = studentList[i].name.toString()
    }

     */

    fun getNameStudent(name: String):String{
        nameStudent = name
        println("student $nameStudent")
        val queryTeam = refTeam.whereEqualTo("members", listOf(nameStudent))

        queryTeam!!.addSnapshotListener { snapshot, e ->
            if (e != null) error = true
            else {
                error = false
                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    //se encontrou um documento que atende a query -> votacao nao foi encerrada
                    queryTeam.get().addOnCompleteListener { querySnapshot ->
                        if(querySnapshot.isSuccessful){
                            for(document in querySnapshot.result){
                                //var i = 0
                                val myTeam = document.toObject(Team::class.java)
                                membersTeam.add(myTeam)
                                Log.d(ContentValues.TAG, " team: ${nameTeams.toString()} - projects: ${nameProjects.toString()} ")
                                return@addOnCompleteListener
                            }
                        } else {
                            Log.d(ContentValues.TAG, " team is not found ")
                        }
                    }
                }

            }
        }

        for(i in membersTeam.indices){
            //selectedItem.add(listItem[position])
            nameTeams.add(membersTeam[i].name.toString())
            nameProjects.add(membersTeam[i].projects.toString())
        }

        return name
    }

    queryStudent!!.addSnapshotListener {snapshot, e ->
        if (e != null) error = true
        else {
            error = false
            if (snapshot != null && snapshot.documents.isNotEmpty()) {
                //se encontrou um documento que atende a query -> votacao nao foi encerrada
                queryStudent.get().addOnCompleteListener {
                        querySnapshot ->
                    if(querySnapshot.isSuccessful){
                        for(document in querySnapshot.result){
                            val myStudent = document.toObject(Student::class.java)
                            studentList.add(myStudent)
                            getNameStudent(myStudent.name.toString())
                            Log.d(ContentValues.TAG, " student: ${studentList.toString()}  ")
                            return@addOnCompleteListener
                        }
                    } else {
                        Log.d(ContentValues.TAG, " student is not found ")
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "aluno(a)")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(10.dp)
                .verticalScroll(rememberScrollState()) //barra de rolagem
        ) {
            //Rows -> corpo do app
           /*
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                Text(
                    text = "meus projetos: ${listOf(nameProjects.toString())}",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )
            }
            Divider(
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
            */

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                //Arrays.toString(arrayName)
                Text(
                    text = "minhas equipes: ${listOf(nameTeams.toString())}",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

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
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                Text(
                    text = "meu perfil:\n email: ${Firebase.auth.currentUser?.email}\n " +
                            "nome: $nameStudent",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                MyLoginButton(text = "sair do app",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(GreenLightCode),
                    onClick = {
                        //fazer logout
                        Firebase.auth.signOut()
                        if(Firebase.auth.currentUser == null){
                            print("sing out")
                            navController.navigate("home")
                        } else {
                            Firebase.auth.signOut()
                        }
                        //print("sing out")
                        //navController.navigate("home")
                    })
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
                    loginStudent = loginStudent,
                    loginTeacher = loginTeacher
                )
            }
        }
    }
}

