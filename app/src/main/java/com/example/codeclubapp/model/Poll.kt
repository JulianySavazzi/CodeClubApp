package com.example.codeclubapp.model

private var identifier = 1

private var qtdTotal: Int = 0
//salvar resultado da votacao do bd -> qtd votos, ganhadores, gerar codigos de autenticacao para votar
//resultPoll -> mostrar lista com a quantidade de votos de cada equipe e de cada projeto
//winnerTeam -> nome da equipe com mais votos
//winnerProject -> nome do projeto com mais votos
//codeVal -> codigo de validacao para usuario poder votar (lista de codigos validos para votar em determinada votacao ->
// a prof vai cadastrar os codigos que podem ser usados na votacao, naão pode ter coódigos repetidos)
class Poll (
    var id: Int= identifier++,
    var codeVal: MutableList<Long>? = null,
    var qtdTotalVotes: Int? = qtdTotal,
    var teamsForVotes: MutableList<Team>? = null,
    var endPoll: Boolean = false,
    //var projectsForVotes: MutableList<Project>? = null,
    //var qtdVotesTeam: Int? = null,
    //var qtdVotesProject: Int? = null,
    //var winnerTeam: String? = null,
    //var winnerProject: String? = null,
    //var resultPoll: MutableList<String>? = null
)


