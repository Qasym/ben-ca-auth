package com.kasymzhan.auth.controller

import com.kasymzhan.auth.data.AccessToken
import com.kasymzhan.auth.data.Roles
import com.kasymzhan.auth.data.User
import com.kasymzhan.auth.data.UserRequest
import com.kasymzhan.auth.service.AuthUserDetailsService
import com.kasymzhan.auth.service.TokenService
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.util.Date

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userDetailsService: AuthUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService,
    private val webClient: WebClient,
) {
    @PostMapping("/register")
    fun register(@RequestBody user: UserRequest): ResponseEntity<String> {
        val res = saveUser(user)
        if (res.statusCode != HttpStatus.CREATED)
            return res
        try {
            signalProcessor(user.name)
        } catch (_: Exception) {}
        return res
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

    private fun saveUser(user: UserRequest): ResponseEntity<String> {
        val userRepository = userDetailsService.userRepository
        if (userRepository.findByName(user.name) != null)
            return ResponseEntity("User already exists", HttpStatus.CONFLICT)
        val dbUser = User(
            id = ObjectId(Date()),
            name = user.name,
            password = passwordEncoder.encode(user.password),
            role = Roles.USER
        )
        userRepository.save(dbUser)
        return ResponseEntity("Successfully created ${user.name}", HttpStatus.CREATED)
    }

    private fun signalProcessor(username: String) {
        val user = userDetailsService.userRepository.findByName(username) ?: return
        val userDetails = userDetailsService.loadUserByUsername(username)
        val roles = mapOf("roles" to listOf(Roles.ADMIN)) // generate admin role
        val token = tokenService // token lives 10 seconds
            .generate(userDetails,Date(Date().time + 10000), roles)
        webClient.post()
            .uri("http://localhost:2002/track/registration/${user.id}")
            .header("Authorization", "Bearer $token")
            .retrieve()
            .bodyToMono<Void>()
            .block()
    }
}