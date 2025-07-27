package org.example.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.util.UUID

class UserSessionRepositoryDbImpl(private val db: Database) : UserSessionRepository {

    object UserSessionTable : Table("user_sessions") {
        val userId = uuid("user_id")
        val currentToken = varchar("current_token", 255)
        val updatedTs = timestamp("updated_ts")
    }

    override suspend fun newSession(userId: String, sessionToken: String) {
        val uuidUserId = UUID.fromString(userId)
        val now = Instant.now()
        newSuspendedTransaction(db = db) {
            val updatedRows = UserSessionTable.update({ UserSessionTable.userId eq uuidUserId }) {
                it[UserSessionTable.currentToken] = sessionToken
                it[UserSessionTable.updatedTs] = now
            }
            if (updatedRows == 0) {
                UserSessionTable.insert {
                    it[UserSessionTable.userId] = uuidUserId
                    it[UserSessionTable.currentToken] = sessionToken
                    it[UserSessionTable.updatedTs] = now
                }
            }
        }
    }

    override suspend fun getUserIdBySessionToken(sessionToken: String): String? {
        return UserSessionTable.select { UserSessionTable.currentToken eq sessionToken }
            .map { it[UserSessionTable.userId].toString() }
            .singleOrNull()
    }

}