package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Teacher
import kotlinx.coroutines.flow.Flow

class TeacherRepository (){
    private val dataSource = DataSource()
    fun saveTeacher(
        name: String,
        email: String,
        pass: String,
        isTeacher: Boolean = true,
        isStudent: Boolean = false
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        dataSource.saveTeacher(name, email, pass, isTeacher, isStudent)
    }

    fun getTeacher(): Flow<MutableList<Teacher>>{
        return dataSource.getTeacher()
    }

}