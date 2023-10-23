package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.LogPoll
import com.example.codeclubapp.model.Poll
import com.example.codeclubapp.model.Team
import kotlinx.coroutines.flow.Flow

class PollRepository {
    private val dataSource = DataSource()

    fun savePoll(
        id: Int,
        codeVal: MutableList<Long>,
        qtdTotalVotes: Int,
        teamsVoted: List<Team>,
        endPoll: Boolean

    ){
        return dataSource.savePoll(id, codeVal, qtdTotalVotes, teamsVoted, endPoll)
    }

    fun saveLog(
        id: Int,
        name: String,
        description: String
    ){
        return dataSource.saveLog(id, name, description)
    }

    fun deletePoll(id: Int){
        return dataSource.deletePoll(id)
    }

    fun updatePoll(
        id: Int,
        endPoll: Boolean
    ){
        return dataSource.updatePoll(id, endPoll)
    }

    fun updateCodValPoll(
        id: Int,
        codeVal: MutableList<Long>
    ){
        return dataSource.updateCodValPoll(id, codeVal)
    }

    fun getPoll(): Flow<MutableList<Poll>> {
        return dataSource.getPoll()
    }

    fun getLogs(): Flow<MutableList<LogPoll>>{
        return dataSource.getLogs()
    }

    fun returnPoll(): MutableList<Poll> {
        return dataSource.returnPoll()
    }

    fun returnOnPoll(): MutableList<Poll> {
        return dataSource.returnOnPoll()
    }

    /*
    fun verifyPoll():Boolean{
        return  dataSource.verifyPoll()
    }
     */

    fun verifyStatusPoll(
        id: Int,
        codeVal: MutableList<Long>,
        qtdTotalVotes: Int,
        teamsVoted: List<Team>,
        endPoll: Boolean
    ){
        return dataSource.verifyStatusPoll(id, codeVal, qtdTotalVotes, teamsVoted, endPoll)
    }
}