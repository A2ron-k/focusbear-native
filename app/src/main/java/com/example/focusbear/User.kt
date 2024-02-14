package com.example.focusbear

data class User(
    val id: Int,
    val username: String,
    val currency: Int,
    val failedSessionCount: Int,
    val totalSessionCount: Int,
    val totalTimeFocused: Int,
    val totalConsecutiveCount: Int
)
