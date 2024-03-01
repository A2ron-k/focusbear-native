package com.example.focusbear

data class Reward(
    val name: String,
    val description: String,
    val imageResourceId: Int
) {
    fun getname():String{
        return name
    }
    fun getID():Int {
        return imageResourceId
    }
}
