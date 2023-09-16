package com.example.codeclubapp.data.model

//crud alunos -> aluno pode participar de varias equipes e varios projetos
//usuario e senha do(a) aluno(a) serao cadastrados pelo(a) prof
//se o aluno tiver conta do google, podera adicionar ao seu login -> email
//depois de entrar no app pela primeira vez com seu usuario e senha

class Student(
    var user: String,
    var password: String,
    var email: String,
    var projects: List<Project>,
    var teams: List<Team>
    ){


}

fun crudStudent(){
    //create
    //reade
    //update
    //delete
}


