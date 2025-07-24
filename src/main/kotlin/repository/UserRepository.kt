package org.example.repository

import org.example.model.User

interface UserRepository {
    suspend fun getUserById(userId: String): User?
}