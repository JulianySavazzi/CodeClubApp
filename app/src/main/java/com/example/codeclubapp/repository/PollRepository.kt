package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Poll
import kotlinx.coroutines.flow.Flow

class PollRepository {
    private val dataSource = DataSource()

    fun getPoll(): Flow<MutableList<Poll>> {
        return dataSource.getPoll()
    }
}