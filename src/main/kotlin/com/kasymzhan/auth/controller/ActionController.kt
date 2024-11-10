package com.kasymzhan.auth.controller

import com.kasymzhan.auth.service.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/action")
class ActionController(private val tokenService: TokenService) {
    @PostMapping("/{someAction}")
    fun doAction(@PathVariable someAction: String, request: HttpServletRequest): String {
        val token = tokenService.tryParseToken(request)
        val username = tokenService.getUsername(token!!)
        return "$username performed $someAction"
    }
}