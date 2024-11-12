package com.kasymzhan.auth.service

import com.kasymzhan.auth.config.JwtConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(private val jwtConfig: JwtConfig) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtConfig.secret.toByteArray()
    )

    private val expirationDate
        get() = Date(Date().time + jwtConfig.expiration)

    fun generate(
        userDetails: UserDetails, expirationDate: Date, additionalClaims: Map<String, Any> = emptyMap()
    ): String = Jwts.builder()
        .claims()
        .subject(userDetails.username)
        .issuedAt(currentTime())
        .expiration(expirationDate)
        .add(additionalClaims)
        .and().signWith(secretKey).compact()

    fun generate(userDetails: UserDetails, additionalClaims: Map<String, Any> = emptyMap()) =
        generate(userDetails, expirationDate, additionalClaims)

    fun getUsername(token: String): String? = getAllClaims(token)?.subject

    fun isExpired(token: String): Boolean = getAllClaims(token)?.expiration?.before(currentTime()) ?: true

    fun tryParseToken(request: HttpServletRequest): String? {
        val authHeader: String? = request.getHeader("Authorization")
        if (!authHeader.containsToken())
            return null
        val token = authHeader!!.extractToken()
        return token
    }

    private fun getAllClaims(token: String): Claims? {
        try {
            val decoder = Jwts.parser().verifyWith(secretKey).build()
            return decoder.parseSignedClaims(token).payload
        } catch (e: Exception) {
            println("exception: $e")
            return null
        }
    }

    private fun currentTime() = Date()

    private fun String?.containsToken() =
        this != null && this.startsWith("Bearer ")

    private fun String.extractToken(): String =
        this.substringAfter("Bearer ")
}