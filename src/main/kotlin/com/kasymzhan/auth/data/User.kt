package com.kasymzhan.auth.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    var id: ObjectId?,
    var name: String,
    var password: String,
    var status: String = Status.NEW,
    var gold: Int = 0,
    var diamond: Int = 0,
    var role: String
)

object Status {
    const val NEW = "NEW"
    const val NOT_NEW = "NOT_NEW"
    const val BANNED = "BANNED"
}

object Roles {
    const val USER = "USER"
    const val ADMIN = "ADMIN"
}