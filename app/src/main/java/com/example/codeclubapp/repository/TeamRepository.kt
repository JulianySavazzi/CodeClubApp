package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Project
import com.example.codeclubapp.model.Student
import com.example.codeclubapp.model.Team
import kotlinx.coroutines.flow.Flow

class TeamRepository() {
    private val dataSource = DataSource()
    //var identifier = 0

    fun saveTeam(
        id: Int ,
        name: String,
        members: MutableList<Student>,
        projects: MutableList<Project>
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        dataSource.saveTeam(id, name, members, projects)
    }

    fun getTeam(): Flow<MutableList<Team>> {
        return dataSource.getTeam()
    }

    fun getTeamByName(name: String): Team{
        return dataSource.getTeamByName(name)
    }

    fun deleteTeam(title: String){
        return dataSource.deleteTeam(title)
    }

    fun updateTeamByName(
        name: String,
        members: MutableList<Student>,
        projects: MutableList<Project>
    ){
        dataSource.updateTeamByName(name, members, projects)
    }
}
