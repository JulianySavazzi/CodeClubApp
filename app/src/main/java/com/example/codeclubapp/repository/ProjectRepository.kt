package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import kotlinx.coroutines.flow.Flow

class ProjectRepository() {
    private val dataSource = DataSource()

    fun saveProject(
        name: String,
        description: String,
        members: MutableList<Student>,
        team: MutableList<Team>
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        dataSource.saveProject(name, description, members, team)
    }

    fun getProject(): Flow<MutableList<Project>> {
        return dataSource.getProject()
    }
}