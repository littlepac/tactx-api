package org.example.service

import org.example.model.User
import org.example.repository.UserRepository

class UserService(private val userRepository: UserRepository) {
    suspend fun findUser(userId: String): User? = userRepository.getUserById(userId)
}