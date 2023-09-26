package com.example.codeclubapp.model

private var identifier = 1
//relacionamento entre projeto e alunos(as) -> equipes
//a equipe pode ter varios projetos
//ela pode ter varios membros
class Team (
    var id: Int= identifier++,
    var name: String? = null,
    var members: MutableList<Student>? = null,
    var projects: MutableList<Project>? = null
)