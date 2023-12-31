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
        projects: MutableList<Project>,
        vote: Int
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        return dataSource.saveTeam(id, name, members, projects, vote)
    }

    fun getTeam(): Flow<MutableList<Team>> {
        return dataSource.getTeam()
    }

    fun returnTeam(): MutableList<Team>{
        return dataSource.returnTeam()
    }

    fun getTeamByName(name: String): Team{
        return dataSource.getTeamByName(name)
    }

    fun deleteTeam(title: String){
        return dataSource.deleteTeam(title)
    }

    fun getStudentTeam(members: String): MutableList<Team>{
        return dataSource.getStudentTeam(members)
    }

    fun updateVoteTeamByName(
        title: String,
        vote: Int
    ){
        return dataSource.updateVoteTeamByName(title, vote)
    }
}
