package com.example.codeclubapp.model

class Student (
    var user: String? = null,
    var password: String? = null,
    var email: String? = null,
    var projects: List<Project>? = null,
    var teams: List<Team>? = null,
    var isTeacher: Boolean = false,
    var isStudent: Boolean = true
)