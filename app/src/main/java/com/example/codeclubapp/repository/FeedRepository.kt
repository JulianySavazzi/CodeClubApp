package com.example.codeclubapp.repository

import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.model.Feed
import kotlinx.coroutines.flow.Flow

class FeedRepository() {
    private val dataSource = DataSource()
    //var identifier = 0

    fun saveFeed(
        id: Int,
        name: String,
        description: String
    ){
        //vamos receber os atributos da view a enviar ao banco de dados
        dataSource.saveFeed(id, name, description)
    }

    fun getFeed(): Flow<MutableList<Feed>> {
        return dataSource.getFeed()
    }

    fun getFeedByName(name: String, description: String): Feed {
        return dataSource.getFeedByName(name, description)
    }

    fun deleteFeed(title: String, description: String){
        return dataSource.deleteFeed(title, description)
    }
}