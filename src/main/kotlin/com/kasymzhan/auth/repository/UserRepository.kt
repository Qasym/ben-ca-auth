package com.kasymzhan.auth.repository

import com.kasymzhan.auth.data.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, ObjectId> {
    fun findByName(username: String): User?
}