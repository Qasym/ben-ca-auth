package com.kasymzhan.auth.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/action")
class ActionController {
    @PostMapping("/{someAction}")
    fun doAction(@PathVariable someAction: String): String {
        return "you performed $someAction"
    }
}