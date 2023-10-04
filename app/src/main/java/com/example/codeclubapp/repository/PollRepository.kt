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
        teamsForVotes: MutableList<Team>,
        endPoll: Boolean

    ){
        dataSource.savePoll(id, codeVal, qtdTotalVotes, teamsForVotes, endPoll)
    }

    fun getPoll(): Flow<MutableList<Poll>> {
        return dataSource.getPoll()
    }
}