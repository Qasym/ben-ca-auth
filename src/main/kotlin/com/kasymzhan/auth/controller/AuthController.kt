package com.kasymzhan.auth.controller

import com.kasymzhan.auth.data.User
import com.kasymzhan.auth.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(val userRepository: UserRepository, val passwordEncoder: BCryptPasswordEncoder) {
    @PostMapping("/register")
    fun register(@RequestBody user: User): HttpStatus {
        println("/auth/register => >$user<")
        user.password = passwordEncoder.encode(user.password)
        userRepository.save(user)
        return HttpStatus.CREATED
    }

    @PostMapping("/login")
    fun login(@RequestBody user: User): HttpStatus {
        return HttpStatus.OK
    }
}