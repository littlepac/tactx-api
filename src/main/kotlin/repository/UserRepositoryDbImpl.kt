package org.example.repository

import org.example.model.http.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.util.*

class UserRepositoryDbImpl(private val db: Database) : UserRepository {

    object UserTable : Table("users") {
        val userId = uuid("user_id")
        val email = varchar("email", 255)
        val userName = varchar("username", 100)
        val createdTs = timestamp("created_ts")
        val updatedTs = timestamp("updated_ts")
        val active = bool("active")
    }

    override suspend fun getUserById(userId: String): User? {
        return newSuspendedTransaction(db = db) {
            UserTable.select { UserTable.userId eq UUID.fromString(userId) }.map {
                User(
                    userId = it[UserTable.userId].toString(),
                    email = it[UserTable.email],
                    userName = it[UserTable.userName],
                    active = it[UserTable.active]
                )
            }
        }.singleOrNull()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return newSuspendedTransaction(db = db) {
            UserTable.select { UserTable.email eq email }.map {
                User(
                    userId = it[UserTable.userId].toString(),
                    email = it[UserTable.email],
                    userName = it[UserTable.userName],
                    active = it[UserTable.active]
                )
            }
        }.singleOrNull()
    }

    override suspend fun createUser(email: String): User {
        val uuid = UUID.randomUUID()
        val now = Instant.now()
        val active = true
        newSuspendedTransaction(db = db) {
            UserTable.insert {
                it[UserTable.userId] = uuid
                it[UserTable.email] = email
                it[UserTable.userName] = email
                it[UserTable.createdTs] = now
                it[UserTable.updatedTs] = now
                it[UserTable.active] = active
            }
        }
        return User(uuid.toString(), email, email, active)
    }
}