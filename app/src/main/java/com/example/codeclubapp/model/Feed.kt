package com.example.codeclubapp.model

private var identifier = 0
data class Feed (
    var id: Int= identifier++,
    var name: String? = null,
    var description: String? = null
)