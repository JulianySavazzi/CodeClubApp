package com.example.codeclubapp.model

private var identifier = 1
//o(a) aluno(a) pode participar de varias equipes e varios projetos
data class Student (
    var id: Int= identifier++,
    var name: String? = null,
    var password: String? = null,
    var email: String? = null,
    //var projects: MutableList<Project>? = null,
    //var teams: MutableList<Team>? = null,
    var isTeacher: Boolean = false,
    var isStudent: Boolean = true
)