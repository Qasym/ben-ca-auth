package com.kasymzhan.auth.config

import com.kasymzhan.auth.service.AuthUserDetailsService
import com.kasymzhan.auth.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: AuthUserDetailsService,
    private val tokenService: TokenService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")
        if (!authHeader.containsToken()) {
            filterChain.doFilter(request, response)
            return
        }
        val token = authHeader!!.extractToken()
        if (tokenService.isExpired(token)) {
            filterChain.doFilter(request, response)
            return
        }
        updateSecurityContext(token, request)
        filterChain.doFilter(request, response)
    }

    private fun updateSecurityContext(token: String, request: HttpServletRequest) {
        val username = tokenService.getUsername(token)
        val userDetails = userDetailsService.loadUserByUsername(username)
        val authentication = UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.authorities
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        if (SecurityContextHolder.getContext().authentication == null)
            SecurityContextHolder.getContext().authentication = authentication
    }

    private fun String?.containsToken() =
        this != null && this.startsWith("Bearer ")

    private fun String.extractToken(): String =
        this.substringAfter("Bearer ")
}