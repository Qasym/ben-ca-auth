package com.kasymzhan.auth.service

import com.kasymzhan.auth.config.JwtConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(jwtConfig: JwtConfig) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtConfig.secret.toByteArray()
    )

    fun generate(
        userDetails: UserDetails, expirationDate: Date, additionalClaims: Map<String, Any> = emptyMap()
    ): String = Jwts.builder()
        .claims()
        .subject(userDetails.username)
        .issuedAt(currentTime())
        .expiration(expirationDate)
        .add(additionalClaims)
        .and().signWith(secretKey).compact()

    fun getSubject(token: String): String? = getAllClaims(token).subject

    fun isExpired(token: String): Boolean = getAllClaims(token).expiration.before(currentTime())

    private fun getAllClaims(token: String): Claims {
        val decoder = Jwts.parser().verifyWith(secretKey).build()
        return decoder.parseSignedClaims(token).payload
    }

    private fun currentTime() = Date()
}