package com.example.codeclubapp.datasource

import com.example.codeclubapp.model.Teacher
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DataSource {

    //iniciar banco de dados -> iniciar a instancia do db
    private val db = FirebaseFirestore.getInstance()

    //flow -> recuperar todo fluxo de tarefas
    //_allTeachers -> estado de fluxo assincrono
    private val _allTeachers = MutableStateFlow<MutableList<Teacher>>(mutableListOf())
    //allTeachers -> observa todos os dados que foram atribuidos para ela
    private val allTeachers: StateFlow<MutableList<Teacher>> = _allTeachers

    //save student ->


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
        }.addOnFailureListener {
            //erro ao salvar
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

    //save project

    //save team


}