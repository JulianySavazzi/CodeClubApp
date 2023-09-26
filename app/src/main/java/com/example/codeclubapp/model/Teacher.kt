package com.example.codeclubapp.model

private var identifier = 1
class Teacher (
    var id: Int = identifier++,
    var name: String? = null,
    var email: String? = null,
    var pass: String? = null,
    var isTeacher: Boolean = true,
    var isStudent: Boolean = false


)