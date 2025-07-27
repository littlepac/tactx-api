package org.example.repository

interface UserSessionRepository {
    suspend fun newSession(userId: String, sessionToken: String)
    suspend fun getUserIdBySessionToken(sessionToken: String) : String?
}