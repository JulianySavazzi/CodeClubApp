package com.example.codeclubapp.model

class Poll (
    var code_val: Float? = null,
    var qtd_total_votes: Int? = null,
    var teams_votes: List<Team>? = null,
    var projects_votes: List<Project>? = null,
    var result_team: String? = null,
    var result_project: String? = null,
    var result: String
)