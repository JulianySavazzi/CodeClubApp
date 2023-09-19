package com.example.codeclubapp.datasource

import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Teacher
import com.example.codeclubapp.model.Team
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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


    //save student -> name: String, password: String, email: String, projects: List<Project>, teams: List<Team>, isTeacher: Boolean = false, isStudent: Boolean = true
    //MutableList permite alterações -> adicionar ou remover items
    //List é imutavel, nao permite alterações na lista
    fun saveStudent(
        name: String,
        email: String,
        pass: String,
        projects: MutableList<Project>,
        teams: MutableList<Team>,
        isTeacher: Boolean = false,
        isStudent: Boolean = true

    ){
        //mapeamento dos campos do documento (tabela)
        val studentMap = hashMapOf(
            "name" to name,
            "email" to email,
            "pass" to pass,
            "projects" to projects,
            "teams" to teams,
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
        db.collection("student").get().addOnCompleteListener{
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

    //save techer -> name: String, email: String, pass: String, isTeacher: Boolean = true, isStudent: Boolean = false
    fun saveTeacher(
        name: String,
        email: String,
        pass: String,
        isTeacher: Boolean = true,
        isStudent: Boolean = false
    ){
        //mapeamento para salvar todos os campos
        val teacherMap = hashMapOf(
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
        db.collection("teacher").get().addOnCompleteListener{
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

    //save publication -> crud_feed

    //save poll

    //save project ->  name: String, description: String, members: MutableList<Student>, team: MutableList<Team>
    fun saveProject(
        name: String,
        description: String,
        members: MutableList<Student>,
        team: MutableList<Team>
    ){
        //mapeamento para salvar todos os campos
        val projectMap = hashMapOf(
            "name" to name,
            "description" to description,
            "members" to members,
            "team" to team

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

    //save team -> name: String, members: MutableList<Student>, projects: MutableList<Project>
    fun saveTeam(
        name: String,
        members: MutableList<Student>,
        projects: MutableList<Project>

    ){
        //mapeamento para salvar todos os campos
        val teamMap = hashMapOf(
            "name" to name,
            "members" to members,
            "projects" to projects

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

}