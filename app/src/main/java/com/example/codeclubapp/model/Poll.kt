package com.example.codeclubapp.model
//import com.example.codeclubapp.repository.TeamRepository

private var identifier = 1
private var qtdTotal: Int = 0
//private var teamRepo = TeamRepository()
//private var listTeams: MutableList<Team> = teamRepo.returnTeam()
//private var listCodeVal: MutableList<Long> = mutableListOf()
//private var listTeams: MutableList<Team> = mutableListOf()

//salvar resultado da votacao no bd -> qtd votos, ganhadores, gerar codigos de autenticacao para votar
//resultPoll -> mostrar lista com a quantidade de votos de cada equipe e de cada projeto
//winnerTeam -> nome da equipe com mais votos
//codeVal -> codigo de validacao para usuario poder votar (lista de codigos validos para votar em determinada votacao ->
// a prof vai cadastrar os codigos que podem ser usados na votacao, naão pode ter códigos repetidos)

data class Poll(
    var id: Int = identifier++,
    var codeVal: MutableList<Long> ? = null,
    var qtdTotalVotes: Int = qtdTotal,
    var teamsVoted: MutableList<Team> ? = null,
    var endPoll: Boolean ? = null
)
