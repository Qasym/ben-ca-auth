package com.kasymzhan.auth.controller

import com.kasymzhan.auth.config.JwtConfig
import com.kasymzhan.auth.data.AccessToken
import com.kasymzhan.auth.data.Roles
import com.kasymzhan.auth.data.User
import com.kasymzhan.auth.data.UserRequest
import com.kasymzhan.auth.service.AuthUserDetailsService
import com.kasymzhan.auth.service.TokenService
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.Date

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userDetailsService: AuthUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService
) {
    @PostMapping("/register")
    fun register(@RequestBody user: UserRequest): HttpStatus {
        val userRepository = userDetailsService.userRepository
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
    @ResponseBody
    fun login(@RequestBody user: UserRequest): AccessToken {
        val authToken = UsernamePasswordAuthenticationToken(user.name, user.password)
        authenticationManager.authenticate(authToken)
        val userDetails = userDetailsService.loadUserByUsername(user.name)
        val roles = mapOf("roles" to userDetails.authorities)
        val jwtToken = tokenService.generate(userDetails, roles)
        return AccessToken(jwtToken)
    }
}