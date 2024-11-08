package com.kasymzhan.auth.repository

import com.kasymzhan.auth.data.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, Long> {
    fun findByName(username: String): User?
}