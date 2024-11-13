package com.kasymzhan.auth.data

data class Reward(
    val id: String,
    val item: String,
    val name: String,
    val quantity: Int
)

object Items {
    const val DIAMONT = "DIAMOND"
    const val GOLD = "GOLD"
}
