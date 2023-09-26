package com.example.codeclubapp.model

private var identifier = 1
class Project (
    var id: Int= identifier++,
    var name: String? = null,
    var description: String? = null
    //var members: MutableList<Student>? = null,
    //var team: MutableList<Team>? = null
)