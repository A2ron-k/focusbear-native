package com.example.focusbear

import java.sql.Date

data class FocusSession(
    val id: Int,
    val timeFocused: Int,
    val date: Date
)
