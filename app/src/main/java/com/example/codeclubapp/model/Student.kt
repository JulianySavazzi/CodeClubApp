package com.example.codeclubapp.model

class Student (
    var name: String? = null,
    var password: String? = null,
    var email: String? = null,
    var projects: MutableList<Project>? = null,
    var teams: MutableList<Team>? = null,
    var isTeacher: Boolean = false,
    var isStudent: Boolean = true
)