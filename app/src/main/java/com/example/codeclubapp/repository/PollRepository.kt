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
<<<<<<< HEAD
        dataSource.savePoll(id, codeVal, qtdTotalVotes, teamsVoted, endPoll)
    }

    fun deletePoll(id: Int){
        dataSource.deletePoll(id)
=======
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
>>>>>>> refs/remotes/origin/master
    }

    fun updatePoll(
        id: Int,
        endPoll: Boolean
    ){
<<<<<<< HEAD
        dataSource.updatePoll(id, endPoll)
=======
        return dataSource.updatePoll(id, endPoll)
    }

    fun updateCodValPoll(
        id: Int,
        codeVal: MutableList<Long>
    ){
        return dataSource.updateCodValPoll(id, codeVal)
    }

    fun updateVotesPoll(
        id: Int,
        qtdTotalVotes: Int,
        teamsVoted: MutableList<Team>
    ){
        return dataSource.updateVotesPoll(id, qtdTotalVotes, teamsVoted)
>>>>>>> refs/remotes/origin/master
    }

    fun getPoll(): Flow<MutableList<Poll>> {
        return dataSource.getPoll()
    }

<<<<<<< HEAD
=======
    fun getPollById(id: Int): Poll{
        return dataSource.getPollById(id)
    }

    fun getLogs(): Flow<MutableList<LogPoll>>{
        return dataSource.getLogs()
    }

>>>>>>> refs/remotes/origin/master
    fun returnPoll(): MutableList<Poll> {
        return dataSource.returnPoll()
    }

<<<<<<< HEAD
=======
    fun returnOnPoll(): MutableList<Poll> {
        return dataSource.returnOnPoll()
    }

    /*
    fun verifyPoll():Boolean{
        return  dataSource.verifyPoll()
    }
     */

>>>>>>> refs/remotes/origin/master
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