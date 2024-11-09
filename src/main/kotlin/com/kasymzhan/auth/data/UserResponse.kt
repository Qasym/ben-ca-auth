package com.kasymzhan.auth.data

import org.bson.types.ObjectId

data class UserResponse(
    val id: ObjectId,
    val name: String,
    val status: String,
    val gold: Int,
    val diamond: Int,
    val role: String
)
