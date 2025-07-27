package org.example.service

import org.example.model.http.User
import org.example.repository.UserRepository

class UserService(private val userRepository: UserRepository, private val userBalanceService: UserBalanceService) {
    suspend fun getUserById(userId: String): User? = userRepository.getUserById(userId)
    suspend fun getUserByEmail(email: String): User? = userRepository.getUserByEmail(email)
    suspend fun createUser(email: String): User {
        val user = userRepository.createUser(email)
        userBalanceService.initUserBalance(user.userId)
        return user
    }
}