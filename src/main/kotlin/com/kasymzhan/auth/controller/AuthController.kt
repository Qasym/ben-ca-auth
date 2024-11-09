package com.kasymzhan.auth.controller

import com.kasymzhan.auth.data.Roles
import com.kasymzhan.auth.data.User
import com.kasymzhan.auth.data.UserRequest
import com.kasymzhan.auth.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.Date

@RestController
@RequestMapping("/auth")
class AuthController(val userRepository: UserRepository, val passwordEncoder: BCryptPasswordEncoder) {
    @PostMapping("/register")
    fun register(@RequestBody user: UserRequest): HttpStatus {
        if (userRepository.findByName(user.name) != null)
            throw ResponseStatusException(HttpStatus.CONFLICT, "User already exists")
        val dbUser = User(
            id = ObjectId(Date()),
            name = user.name,
            password = passwordEncoder.encode(user.password),
            role = Roles.USER
        )
        userRepository.save(dbUser)
        return HttpStatus.CREATED
    }

    @PostMapping("/login")
    fun login(@RequestBody user: User): HttpStatus {
        return HttpStatus.OK
    }
}