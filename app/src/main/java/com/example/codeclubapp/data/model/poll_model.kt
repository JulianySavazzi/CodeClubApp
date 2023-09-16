package com.example.codeclubapp.data.model

//crud votacao
//para poder votar, o usuario recebera um codigo gerado pelo(a) prof
//o codigo sera validado para aparecer as opcoes em que o usuario pode votar
//o voto sera contabilizado, e aparecera um alert indicando que o voto foi salvo com sucesso
//apos votar o app voltara para a tela home
//cada codigo pode ser utilizado uma unica vez -> controle de vezes em que cada usuario vai votar
//o resultado da votacao vai gerar uma publicacao no feed -> crud_feed

class Poll(
    var code_val: Float,
    var qtd_total_votes: Int,
    var teams_votes: List<Team>,
    var projects_votes: List<Project>,
    var result_team: String,
    var result_project: String,
    var result: String
){

}

fun crudPoll(){
    //create
    //reade
    //update
    //delete
}


