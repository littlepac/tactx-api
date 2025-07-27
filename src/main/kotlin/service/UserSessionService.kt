package org.example.service

import org.example.repository.UserSessionRepository
import org.example.model.http.User
import org.example.repository.UserRepository
import java.util.UUID

class UserSessionService(
    private val userService: UserService,
    private val userSessionRepository: UserSessionRepository
) {
    suspend fun loginUser(email: String): String {
        (userService.getUserByEmail(email) ?: userService.createUser(email)).let {
            val sessionToken = UUID.randomUUID().toString()
            userSessionRepository.newSession(it.userId, sessionToken)
            return sessionToken
        }
    }

    suspend fun getUserBySession(sessionToken: String): User? {
        return userSessionRepository.getUserIdBySessionToken(sessionToken)?.let {
            return userService.getUserById(it)!!
        }
    }

}