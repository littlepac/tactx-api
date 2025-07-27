package org.example.repository

import org.example.model.http.User

interface UserRepository {
    suspend fun getUserById(userId: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun createUser(email: String): User
}