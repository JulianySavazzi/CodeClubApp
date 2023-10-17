package com.example.codeclubapp.model

private var identifier = 1

private var votes = 0
//relacionamento entre projeto e alunos(as) -> equipes
//a equipe pode ter varios projetos
//ela pode ter varios membros
data class Team (
    var id: Int= identifier++,
    var name: String? = null,
    var members: MutableList<Student>? = null,
    var projects: MutableList<Project>? = null,
    var vote: Int = votes
)