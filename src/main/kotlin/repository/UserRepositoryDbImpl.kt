package org.example.repository

import org.example.model.User

class UserRepositoryDbImpl : UserRepository {
    private val dummyUsers = listOf(
        User("a", "alice@example.com", "alice", true),
        User("b", "bob@example.com", "bob", true)
    )

    override suspend fun getUserById(userId: String): User? {
        println(dummyUsers)
        return dummyUsers.find { it.userId == userId }
    }
}