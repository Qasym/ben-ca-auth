package com.kasymzhan.auth.controller

import com.kasymzhan.auth.data.Items
import com.kasymzhan.auth.data.Reward
import com.kasymzhan.auth.data.User
import com.kasymzhan.auth.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reward")
class RewardController(
    private val userRepository: UserRepository,
) {
    @PostMapping("/{id}")
    fun rewardUser(@PathVariable id: String, @RequestBody reward: Reward): ResponseEntity<String> {
        val user = userRepository.findById(ObjectId(id)).get()
        if (reward.item == Items.GOLD)
            user.gold += reward.quantity
        else if (reward.item == Items.DIAMONT)
            user.diamond += reward.quantity
        userRepository.save(user)
        return ResponseEntity("Updated user", HttpStatus.OK)
    }
}