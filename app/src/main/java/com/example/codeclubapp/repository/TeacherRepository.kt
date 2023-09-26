package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Teacher
import kotlinx.coroutines.flow.Flow

class TeacherRepository(){
    private val dataSource = DataSource()
    //var identifier = 0
    fun saveTeacher(
        id: Int ,
        name: String,
        email: String,
        pass: String,
        isTeacher: Boolean = true,
        isStudent: Boolean = false
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        dataSource.saveTeacher(id, name, email, pass, isTeacher, isStudent)
    }

    fun getTeacher(): Flow<MutableList<Teacher>>{
        return dataSource.getTeacher()
    }

    fun verifyTeacherLogin(email: String, pass: String){
        return dataSource.verifyTeacherLogin(email, pass)
    }

}