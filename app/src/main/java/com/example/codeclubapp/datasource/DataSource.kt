package com.example.codeclubapp.datasource

import android.os.Build
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.codeclubapp.model.Feed
import com.example.codeclubapp.model.Poll
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Teacher
import com.example.codeclubapp.model.Team
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DataSource {

    //iniciar banco de dados -> iniciar a instancia do db
    private val db = FirebaseFirestore.getInstance()

    //flow -> recuperar todo fluxo de tarefas TEACHER
    //_allTeachers -> estado de fluxo assincrono
    private val _allTeachers = MutableStateFlow<MutableList<Teacher>>(mutableListOf())
    //allTeachers -> observa todos os dados que foram atribuidos para ela
    private val allTeachers: StateFlow<MutableList<Teacher>> = _allTeachers

    //flow -> recuperar todo fluxo de tarefas STUDENT
    //_allStudents -> estado de fluxo assincrono
    private val _allStudents = MutableStateFlow<MutableList<Student>>(mutableListOf())
    //allStudents -> observa todos os dados que foram atribuidos para ela
    private val allStudents: StateFlow<MutableList<Student>> = _allStudents

    //flow -> recuperar todo fluxo de tarefas PROJECT
    //_allProjects -> estado de fluxo assincrono
    private val _allProjects = MutableStateFlow<MutableList<Project>>(mutableListOf())
    //allProjects -> observa todos os dados que foram atribuidos para ela
    private val allProjects: StateFlow<MutableList<Project>> = _allProjects

    //flow -> recuperar todo fluxo de tarefas TEAM
    //_allTeams -> estado de fluxo assincrono
    private val _allTeams = MutableStateFlow<MutableList<Team>>(mutableListOf())
    //allTeams -> observa todos os dados que foram atribuidos para ela
    private val allTeams: StateFlow<MutableList<Team>> = _allTeams

    //flow -> recuperar todo fluxo de tarefas FEED
    //_allFeeds -> estado de fluxo assincrono
    private val _allFeeds = MutableStateFlow<MutableList<Feed>>(mutableListOf())
    //allFeeds -> observa todos os dados que foram atribuidos para ela
    private val allFeeds: StateFlow<MutableList<Feed>> = _allFeeds

    //flow -> recuperar todo fluxo de tarefas POLL
    //_allPolls -> estado de fluxo assincrono
    private val _allPolls = MutableStateFlow<MutableList<Poll>>(mutableListOf())
    //allPolls -> observa todos os dados que foram atribuidos para ela
    private val allPolls: StateFlow<MutableList<Poll>> = _allPolls

    //utilizar firebase auth
    private val auth = FirebaseAuth.getInstance()

    private var student = Student()

    private var feed = Feed()

    private var project = Project()

    private var team = Team()

    private var currentPoll: Poll = Poll()

    //var isTrue = false
    //criar estado para a variável:
    //private var isTrue by remember {mutableStateOf(false)}

    //*************************************************** STUDENT ***************************************************
    //save student -> name: String, password: String, email: String,isTeacher: Boolean = false, isStudent: Boolean = true
    //projects: List<Project>, teams: List<Team>
    //MutableList permite alterações -> adicionar ou remover items
    //List é imutavel, nao permite alterações na lista
    @JvmOverloads
    fun saveStudent(
        id: Int,
        name: String,
        email: String,
        pass: String,
        //projects: MutableList<Project>,
        //teams: MutableList<Team>,
        isTeacher: Boolean = false,
        isStudent: Boolean = true

    ){
        //mapeamento dos campos do documento (tabela)
        val studentMap = hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "pass" to pass,
            //"projects" to projects,
            //"teams" to teams,
            "isTeacher" to isTeacher,
            "isStudent" to isStudent
        )

        //salvar colecao student -> document student
        db.collection("student").document(email).set(studentMap).addOnCompleteListener{
            print("success save student")
        }.addOnFailureListener {
            print("fail save student")
        }
    }

    //get student -> recuperar dados do aluno
    fun getSudent(): Flow<MutableList<Student>>{
        val listStudent: MutableList<Student> = mutableListOf()
        //listar todos os alunos cadastrados
        db.collection("student").whereIn("isStudent", listOf(true)).get().addOnCompleteListener{
                querySnapshot ->
            if(querySnapshot.isSuccessful){
                for(document in querySnapshot.result){
                    //se a colecao existe e tem documentos
                    //vamos recuperar cada documento e adicionar no nosso objeto da model
                    val student = document.toObject(Student::class.java)
                    listStudent.add(student)
                    _allStudents.value = listStudent

                }
            }
        }
        return allStudents
    }

    //exemplo:
    /*
        val docRef = db.collection("cities").document("SF")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
            } else {
                Log.d(TAG, "Current data: null")
            }
        }




        val docRef = db.collection("cities").document("SF")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

    * */


    fun getSudentByName(name: String): Student {
        db.collection("student")
            .whereIn("isStudent", listOf(true))
            .whereIn("name", listOf(name))
            .get().addOnCompleteListener {
                    querySnapshot ->
                if(querySnapshot.isSuccessful){
                    for(document in querySnapshot.result){
                        student = document.toObject(Student::class.java)
                        Log.d(TAG, "student by name: $querySnapshot, $student")
                        return@addOnCompleteListener
                    }
                }
            }
        return student
    }

    fun getSudentByEmail(email: String): Student {
        db.collection("student")
            .whereIn("isStudent", listOf(true))
            .whereIn("email", listOf(email))
            .get().addOnCompleteListener {
                    querySnapshot ->
                if(querySnapshot.isSuccessful){
                    for(document in querySnapshot.result){
                        student = document.toObject(Student::class.java)
                        Log.d(TAG, "student by email: $querySnapshot, ${student.name}")
                        return@addOnCompleteListener
                    }
                }
            }
        return student
    }

    fun verifyStudent(email: String, pass: String){
        db.collection("student")
            .whereIn("isStudent", listOf(true))
            .whereIn("email", listOf(email))
            .whereIn("pass", listOf(pass))
            .get().addOnCompleteListener{
                    querySnapshot ->
                if(querySnapshot.isSuccessful){
                    for(document in querySnapshot.result){
                        auth.signInWithEmailAndPassword(email, pass)
                        Log.d(TAG, "auth ok student, current user: ${auth.currentUser}")
                    }
                }
            }
    }

    //delete feed -> deletar publicacao
    fun deleteStudent(email: String){
        val user = Firebase.auth.currentUser!!

        //verificar se o estudante esta cadastrado em alguma equipe antes de excluir
        db.collection("student")
            .document(email).delete().addOnCompleteListener {
                if(user.email == email){
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User account $email deleted.")
                        }
                    }
                }
                Log.d(TAG, "this student is deleted! print ${user.email}")
            }.addOnFailureListener {
                Log.d(TAG, "student not deleted!")
            }

    }


    //*************************************************** TEATCHER ***************************************************
    //save techer -> name: String, email: String, pass: String, isTeacher: Boolean = true, isStudent: Boolean = false
    fun saveTeacher(
        id: Int,
        name: String,
        email: String,
        pass: String,
        isTeacher: Boolean = true,
        isStudent: Boolean = false
    ){
        //mapeamento para salvar todos os campos
        val teacherMap = hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "pass" to pass,
            "isTeacher" to isTeacher,
            "isStudent" to isStudent
        )

        //salvar colecao de teacher em um documento -> como se fosse a tabela teacher
        db.collection("teacher").document(email).set(teacherMap).addOnCompleteListener {
            //salvo com sucesso
            print("success save teacher")
        }.addOnFailureListener {
            //erro ao salvar
            print("fail save teacher")
        }
    }

    //get teacher -> recuperar dados do professor
    fun getTeacher(): Flow<MutableList<Teacher>>{
        val listTeacher: MutableList<Teacher> = mutableListOf()
        //listar todos os professores cadastrados
        db.collection("teacher").whereIn("isTeacher", listOf(true)).get().addOnCompleteListener{
                querySnapshot ->
            if(querySnapshot.isSuccessful){
                for(document in querySnapshot.result){
                    //se a colecao existe e tem documentos
                    //vamos recuperar cada documento e adicionar no nosso objeto da model
                    val teacher = document.toObject(Teacher::class.java)
                    listTeacher.add(teacher)
                    _allTeachers.value = listTeacher

                }
            }
        }
        return allTeachers
    }

    //  query example:
    // Create a reference to the cities collection
    // val citiesRef = db.collection("cities")
    // val notCapitalQuery = citiesRef.whereNotEqualTo("capital", false)
    // citiesRef.whereIn("country", listOf("USA", "Japan"))
    fun verifyTeacherLogin(email: String, pass: String){
        db.collection("teacher")
            .whereIn("isTeacher", listOf(true))
            .whereIn("email", listOf(email))
            .whereIn("pass", listOf(pass))
            .get().addOnCompleteListener{
                    querySnapshot ->
                if(querySnapshot.isSuccessful){
                    for(document in querySnapshot.result){
                        auth.signInWithEmailAndPassword(email, pass)
                        Log.d(TAG, "auth ok teacher, current user: ${auth.currentUser}")
                    }
                }
            }
    }

    //*************************************************** FEED ***************************************************
    //save publication -> crud_feed -> name: String, description: String
    fun saveFeed(
        id: Int,
        name: String,
        description: String
    ){
        //mapeamento para salvar todos os campos
        val feedMap = hashMapOf(
            "id" to id,
            "name" to name,
            "description" to description
        )

        db.collection("feed").document(name).set(feedMap).addOnCompleteListener {
            //salvo com sucesso
            print("success save feed")
        }.addOnFailureListener {
            //erro ao salvar
            print("fail save feed")
        }

    }

    //get feed -> recuperar dados da publicacao
    fun getFeed(): Flow<MutableList<Feed>>{
        val listFeed: MutableList<Feed> = mutableListOf()
        //listar todos as publicacoes cadastrados
        db.collection("feed").get().addOnCompleteListener{
                querySnapshot ->
            if(querySnapshot.isSuccessful){
                for(document in querySnapshot.result){
                    //se a colecao existe e tem documentos
                    //vamos recuperar cada documento e adicionar no nosso objeto da model
                    val project = document.toObject(Feed::class.java)
                    listFeed.add(project)
                    _allFeeds.value = listFeed

                }
            }
        }
        return allFeeds
    }

    fun getFeedByName(name: String, description: String): Feed {
        db.collection("feed")
            .whereIn("name", listOf(name))
            .whereIn("description", listOf(description))
            .get().addOnCompleteListener {
                    querySnapshot ->
                if(querySnapshot.isSuccessful){
                    for(document in querySnapshot.result){
                        feed = document.toObject(Feed::class.java)
                        Log.d(TAG, "feed by name: ${querySnapshot.result}, ${feed.name.toString()}, ${feed.description.toString()}")
                        return@addOnCompleteListener
                    }
                } else {
                    db.collection("feed")
                        .document(name).get().addOnSuccessListener {
                                documentSnapshot ->
                            val myFeed = documentSnapshot.toObject<Feed>()
                            feed = myFeed!!
                        }

                }
            }
        return feed
    }

    fun updateFeed(title: String, description: String){

    }

    //delete feed -> deletar publicacao
    fun deleteFeed(title: String, description: String){
        /*
        var publi = getFeedByName(title, description)
        if(publi.name.toString() == title && publi.description.toString() == description && publi.name.toString() != null && publi.description.toString() != null ){
            Log.d(TAG, "THIS FEED")
        } else {
            Log.d(TAG, "THIS FEED IS NULL")
        }
         */
        db.collection("feed")
            .document(title).delete().addOnCompleteListener {
                Log.d(TAG, "feed is deleted!")
            }.addOnFailureListener {
                Log.d(TAG, "feed not deleted!")
            }
    }

    //*************************************************** PROJECT ***************************************************
    //save project ->  name: String, description: String
    //members: MutableList<Student>, team: MutableList<Team>
    fun saveProject(
        id: Int,
        name: String,
        description: String
        //members: MutableList<Student>,
        //team: MutableList<Team>
    ){
        //mapeamento para salvar todos os campos
        val projectMap = hashMapOf(
            "id" to id,
            "name" to name,
            "description" to description,
            //"members" to members,
            //"team" to team
        )

        //salvar colecao de project em um documento -> como se fosse a tabela teacher
        db.collection("project").document(name).set(projectMap).addOnCompleteListener {
            //salvo com sucesso
            print("success save project")
        }.addOnFailureListener {
            //erro ao salvar
            print("fail save project")
        }
    }

    //get project -> recuperar dados do projeto
    fun getProject(): Flow<MutableList<Project>>{
        val listProject: MutableList<Project> = mutableListOf()
        //listar todos os projetos cadastrados
        db.collection("project").get().addOnCompleteListener{
                querySnapshot ->
            if(querySnapshot.isSuccessful){
                for(document in querySnapshot.result){
                    //se a colecao existe e tem documentos
                    //vamos recuperar cada documento e adicionar no nosso objeto da model
                    val project = document.toObject(Project::class.java)
                    listProject.add(project)
                    _allProjects.value = listProject

                }
            }
        }
        return allProjects
    }

    fun getProjectByName(name: String, description: String): Project {
        db.collection("project")
            .whereIn("name", listOf(name))
            .whereIn("description", listOf(description))
            .get().addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    for (document in querySnapshot.result) {
                        project = document.toObject(Project::class.java)
                        Log.d(TAG, "project by name: ${querySnapshot.result}, ${project.name.toString()}, description: ${project.description.toString()}")
                        return@addOnCompleteListener
                    }
                } else {
                    db.collection("feed")
                        .document(name).get().addOnSuccessListener { documentSnapshot ->
                            val myProject = documentSnapshot.toObject<Project>()
                            project = myProject!!
                            Log.d(TAG, "project by name: ${querySnapshot.result}, ${project.name.toString()}, ${project.description.toString()}")
                        }
                }
            }
        return project
    }


    fun verifyProjectDelete(title: String, description: String): Team {
        //verificar se o projeto esta cadastrado em alguma equipe antes de excluir
        val myProject = getProjectByName(title, description)

        db.collection("team")
            .whereIn("projects", listOf(myProject))
            .get().addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    for (document in querySnapshot.result) {
                        team = document.toObject(Team::class.java)
                        Log.d(
                            TAG,
                            " can't delete this project, because this exists in team: ${querySnapshot.result}, ${team.name.toString()}, ${team.projects.toString()} , ${team.members.toString()}"
                        )
                        return@addOnCompleteListener
                    }
                } else {
                    team = Team()
                }
            }
        return this.team
    }

    fun deleteProject(title: String, description: String){
        //se o projeto selecionado nao pertencer a nenhuma equipe ele pode ser excluido
        db.collection("project")
            .document(title).delete().addOnCompleteListener {
                Log.d(TAG, "project is deleted!")
            }.addOnFailureListener {
                Log.d(TAG, "project not deleted!")
            }
    }


    //*************************************************** TEAM ***************************************************
    //save team -> name: String, members: MutableList<Student>, projects: MutableList<Project>
    fun saveTeam(
        id: Int,
        name: String,
        members: MutableList<Student>,
        projects: MutableList<Project>,
        vote: Int

    ){
        //mapeamento para salvar todos os campos
        val teamMap = hashMapOf(
            "id" to id,
            "name" to name,
            "members" to members,
            "projects" to projects,
            "vote" to vote

        )

        //salvar colecao de team em um documento -> como se fosse a tabela team
        db.collection("team").document(name).set(teamMap).addOnCompleteListener {
            //salvo com sucesso
            print("success save team")
        }.addOnFailureListener {
            //erro ao salvar
            print("fail save team")
        }
    }

    //get team -> recuperar dados da equipe
    fun getTeam(): Flow<MutableList<Team>>{
        val listTeam: MutableList<Team> = mutableListOf()
        //listar todos os projetos cadastrados
        db.collection("team").get().addOnCompleteListener{
                querySnapshot ->
            if(querySnapshot.isSuccessful){
                for(document in querySnapshot.result){
                    //se a colecao existe e tem documentos
                    //vamos recuperar cada documento e adicionar no nosso objeto da model
                    val team = document.toObject(Team::class.java)
                    listTeam.add(team)
                    _allTeams.value = listTeam

                }
            }
        }
        return allTeams
    }

    fun returnTeam(): MutableList<Team>{
        val listTeam: MutableList<Team> = mutableListOf()
        //listar todos os projetos cadastrados
        db.collection("team").get().addOnCompleteListener{
                querySnapshot ->
            if(querySnapshot.isSuccessful){
                for(document in querySnapshot.result){
                    //se a colecao existe e tem documentos
                    //vamos recuperar cada documento e adicionar no nosso objeto da model
                    val team = document.toObject(Team::class.java)
                    listTeam.add(team)
                    _allTeams.value = listTeam
                }
            }
        }
        return allTeams.value
    }

    fun getTeamByName(name: String): Team{
        db.collection("team")
            .whereIn("name", listOf(name))
            .get().addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    for (document in querySnapshot.result) {
                        team = document.toObject(Team::class.java)
                        Log.d(
                            TAG,
                            "team by name: ${querySnapshot.result}, ${team.name.toString()}, ${team.projects.toString()} , ${team.members.toString()}"
                        )
                        return@addOnCompleteListener
                    }
                } else {
                    db.collection("team")
                        .document(name).get().addOnSuccessListener { documentSnapshot ->
                            val myTeam = documentSnapshot.toObject<Team>()
                            team = myTeam!!
                            Log.d(TAG, "team by name: ${team.name.toString()}, ${team.projects.toString()} , ${team.members.toString()}")
                            //Log.d(TAG, "project by name: $team")
                        }
                }
            }

        return team
    }


    fun deleteTeam(title: String){
        db.collection("team")
            .document(title).delete().addOnCompleteListener {
                Log.d(TAG, "team is deleted!")
            }.addOnFailureListener {
                Log.d(TAG, "team not deleted!")
            }
    }

    fun updateVoteTeamByName(
        name: String,
        members: MutableList<Student>,
        projects: MutableList<Project>,
        vote: Int
    ){
        //mapeamento para salvar todos os campos
        val teamUpMap = hashMapOf(
            "name" to name,
            "members" to members,
            "projects" to projects,
            "vote" to vote
        )

        if(members == members && projects == projects && name == name){
            db.collection("team").document(name).update( "members", members)
            db.collection("team").document(name).update( "projects", projects)
            db.collection("team").document(name).update(teamUpMap)
        }

    }


    //*************************************************** POLL ***************************************************
    //save poll

    //save poll -> id: Int, codeVal: MutableList<Long>, qtdTotalVotes: Int, teamsForVotes: MutableList<Team>, endPoll: Boolean = false,
    fun savePoll(
        id: Int,
        codeVal: MutableList<Long>,
        qtdTotalVotes: Int,
        teamsVoted: List<Team>,
        endPoll: Boolean

    ){
        //mapeamento para salvar todos os campos
        val teamMap = hashMapOf(
            "id" to id,
            "codeVal" to codeVal,
            "qtdTotalVotes" to qtdTotalVotes,
            "teamsVoted" to teamsVoted,
            "endPoll" to endPoll

        )

        //salvar colecao de team em um documento -> como se fosse a tabela team
        db.collection("poll").document(id.toString()).set(teamMap).addOnCompleteListener {
            //salvo com sucesso
            print("success save poll")
        }.addOnFailureListener {
            //erro ao salvar
            print("fail save poll")
        }
    }

    fun deletePoll(id: Int){
        db.collection("poll")
            .document(id.toString()).delete().addOnCompleteListener {
                Log.d(TAG, "poll is deleted!")
            }.addOnFailureListener {
                Log.d(TAG, "poll not deleted!")
            }
    }

    fun getPoll(): Flow<MutableList<Poll>>{
        val listPoll: MutableList<Poll> = mutableListOf()
        //listar todas as votacoes cadastradas
        db.collection("poll").get().addOnCompleteListener{
                querySnapshot ->
            if(querySnapshot.isSuccessful){
                for(document in querySnapshot.result){
                    //se a colecao existe e tem documentos
                    //vamos recuperar cada documento e adicionar no nosso objeto da model
                    val poll = document.toObject(Poll::class.java)
                    listPoll.add(poll)
                    _allPolls.value = listPoll
                }
            }
        }
        return allPolls
    }

    fun returnPoll(): MutableList<Poll> {
        val listPoll: MutableList<Poll> = mutableListOf()
        //listar todas as votacoes cadastradas
        db.collection("poll").get().addOnCompleteListener{
                querySnapshot ->
            if(querySnapshot.isSuccessful){
                for(document in querySnapshot.result){
                    //se a colecao existe e tem documentos
                    //vamos recuperar cada documento e adicionar no nosso objeto da model
                    val poll = document.toObject(Poll::class.java)
                    listPoll.add(poll)
                    _allPolls.value = listPoll
                }
            }
        }
        return allPolls.value
    }

    fun verifyStatusPoll(
        id: Int,
        codeVal: MutableList<Long>,
        qtdTotalVotes: Int,
        teamsVoted: List<Team>,
        endPoll: Boolean
    ){
        db.collection("poll")
            .whereIn("endPoll", listOf(false))
            .get().addOnCompleteListener{
                    querySnapshot ->
                if(querySnapshot.isSuccessful){
                    for(document in querySnapshot.result){
                        currentPoll = document.toObject(Poll::class.java)
                        Log.d(TAG, "endPoll: ${currentPoll.endPoll}, await current poll end for start other poll!")
                        return@addOnCompleteListener
                    }
                } else {
                    Log.d(TAG, "endPoll: ${currentPoll.endPoll}, you can start poll")
                    savePoll(id, codeVal, qtdTotalVotes, teamsVoted, endPoll)
                }
            }
    }


    fun updatePoll(
        id: Int,
        endPoll: Boolean
    ){
        //update poll status
        db.collection("poll").document(id.toString()).update("endPoll", endPoll).
                addOnCompleteListener{
                    Log.d(TAG, "endPoll: ${currentPoll.endPoll}, this poll is finished!")
                }
    }
} //DataSource




