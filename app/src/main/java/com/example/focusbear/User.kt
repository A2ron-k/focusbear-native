package com.example.focusbear

data class User(
    val id: Int,
    val username: String,
    var currency: Int,
    var failedSessionCount: Int,
    var totalSessionCount: Int,
    var totalTimeFocused: Int,
    var totalConsecutiveCount: Int
)
