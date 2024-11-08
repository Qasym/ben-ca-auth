package com.kasymzhan.auth.service

import com.kasymzhan.auth.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService(val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username.isNullOrBlank()) throw Exception("username is null or blank!")
        val user = userRepository.findByName(username)
            ?: throw Exception("User >$username< is not found")
        val userDetails = User.withUsername(username)
            .password(user.password)
            .roles(user.roles.first())
            .build()
        return userDetails
    }
}