package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import kotlinx.coroutines.flow.Flow

class StudentRepository() {
    private val dataSource = DataSource()

    fun saveStudent(
        name: String,
        email: String,
        pass: String,
        projects: MutableList<Project>,
        teams: MutableList<Team>,
        isTeacher: Boolean = false,
        isStudent: Boolean = true
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        dataSource.saveStudent(name, email, pass, projects, teams, isTeacher, isStudent)
    }

    fun getStudent(): Flow<MutableList<Student>> {
        return dataSource.getSudent()
    }
}