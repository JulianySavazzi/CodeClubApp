package com.example.codeclubapp.model

private var identifier = 1
//salvar resultado da votacao do bd -> qtd votos, ganhadores
class Poll (
    var id: Int= identifier++,
    var code_val: Float? = null,
    var qtd_total_votes: Int? = null,
    var teams_votes: MutableList<Team>? = null,
    var projects_votes: MutableList<Project>? = null,
    var qtd_votes_team: Int? = null,
    var qtd_votes_project: Int? = null,
    var result_team: String? = null,
    var result_project: String? = null,
    var result: String
)