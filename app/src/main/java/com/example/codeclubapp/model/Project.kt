package com.example.codeclubapp.model

class Project (
    var name: String? = null,
    var description: String? = null,
    var members: List<Student>? = null,
    var team: List<Team>? = null
)