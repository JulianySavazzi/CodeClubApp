package com.example.codeclubapp.datasource

import com.google.firebase.firestore.FirebaseFirestore

class DataSource {

    //iniciar banco de dados -> iniciar a instancia do db
    private val db = FirebaseFirestore.getInstance()

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

    //save publication -> crud_feed

    //save poll

    //save project

    //save team


}