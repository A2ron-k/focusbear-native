package com.example.focusbear

data class Item(
    val id: Int,
    val name: String,
    val image: String,
    val price: Int,
    //1 means purchased, 0 means not purchased
    var isPurchased: Int
)
