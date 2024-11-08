package com.kasymzhan.auth.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: Long,
    var name: String,
    var password: String,
    var status: Int = UserStatus.NEW,
    var gold: Int = 0,
    var diamond: Int = 0,
    var roles: MutableList<String> = mutableListOf(UserRoles.REGULAR)
)

object UserStatus {
    const val NEW = 0
    const val NOT_NEW = 1
    const val BANNED = 2
}

object UserRoles {
    const val REGULAR = "regular_user"
    const val ADMIN = "admin_user"
}