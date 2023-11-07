package com.example.codeclubapp.model

private var identifier = 1

data class LogPoll (
    var id: Int= identifier++,
    var name: String? = null,
    var description: String? = null
)