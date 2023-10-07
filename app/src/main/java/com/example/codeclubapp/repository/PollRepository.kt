package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
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
        dataSource.savePoll(id, codeVal, qtdTotalVotes, teamsVoted, endPoll)
    }

    fun deletePoll(id: Int){
        dataSource.deletePoll(id)
    }

    fun getPoll(): Flow<MutableList<Poll>> {
        return dataSource.getPoll()
    }

    fun verifyStatusPoll(): Poll{
        return dataSource.verifyStatusPoll()
    }
}