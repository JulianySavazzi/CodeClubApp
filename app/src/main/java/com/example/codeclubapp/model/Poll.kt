package com.example.codeclubapp.model

import com.example.codeclubapp.repository.PollRepository
import java.lang.reflect.Constructor

val repository = PollRepository()

private var identifier = 1

private var qtdTotal: Int = 0

private var listTeams: MutableList<Team> = mutableListOf()

private var listCodeVal: MutableList<Long> = mutableListOf()

//salvar resultado da votacao do bd -> qtd votos, ganhadores, gerar codigos de autenticacao para votar
//resultPoll -> mostrar lista com a quantidade de votos de cada equipe e de cada projeto
//winnerTeam -> nome da equipe com mais votos
//winnerProject -> nome do projeto com mais votos
//codeVal -> codigo de validacao para usuario poder votar (lista de codigos validos para votar em determinada votacao ->
// a prof vai cadastrar os codigos que podem ser usados na votacao, naão pode ter coódigos repetidos)
class Poll (

    var id: Int= identifier++,
    var codeVal: MutableList<Long> = listCodeVal,
    var qtdTotalVotes: Int = qtdTotal,
    var teamsForVotes: MutableList<Team> = listTeams,
    var endPoll: Boolean = false,
    //var projectsForVotes: MutableList<Project>? = null,
    //var qtdVotesTeam: Int? = null,
    //var qtdVotesProject: Int? = null,
    //var winnerTeam: String? = null,
    //var winnerProject: String? = null,
    //var resultPoll: MutableList<String>? = null

)


