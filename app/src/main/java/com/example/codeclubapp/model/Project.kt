package com.example.codeclubapp.model

class Project (
    var name: String? = null,
    var description: String? = null,
    var members: MutableList<Student>? = null,
    var team: MutableList<Team>? = null
)