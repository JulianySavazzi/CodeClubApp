package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import kotlinx.coroutines.flow.Flow

class TeamRepository() {
    private val dataSource = DataSource()

    fun saveTeam(
        name: String,
        members: MutableList<Student>,
        projects: MutableList<Project>
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        dataSource.saveTeam(name, members, projects)
    }

    fun getTeam(): Flow<MutableList<Team>> {
        return dataSource.getTeam()
    }
}