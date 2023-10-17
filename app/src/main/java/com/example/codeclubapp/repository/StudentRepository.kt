package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import kotlinx.coroutines.flow.Flow

class StudentRepository() {
    private val dataSource = DataSource()
    //var identifier = 0

    fun saveStudent(
        id: Int ,
        name: String,
        email: String,
        pass: String,
        //projects: MutableList<Project>,
        //teams: MutableList<Team>,
        isTeacher: Boolean = false,
        isStudent: Boolean = true
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        return dataSource.saveStudent(id, name, email, pass, isTeacher, isStudent)
    }

    fun getStudent(): Flow<MutableList<Student>> {
        return dataSource.getSudent()
    }

    fun getSudentByName(name: String): Student {
        return dataSource.getSudentByName(name)
    }

    fun getSudentByEmail(email: String): Student {
        return dataSource.getSudentByEmail(email)
    }

    fun returnSudent(): MutableList<Student>{
        return dataSource.returnSudent()
    }

    fun verifyStudent(email: String, pass: String){
        return dataSource.verifyStudent(email, pass)
    }

    fun deleteStudent(email: String){
        return dataSource.deleteStudent(email)
    }

}