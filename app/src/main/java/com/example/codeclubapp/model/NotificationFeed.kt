package com.example.codeclubapp.model

private var identifier = 0

data class NotificationFeed (
    //message document in database
    var id: Int= identifier++,
    var content: String? = null,
)

