package org.example.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

class UserPickRepositoryDbImpl(private val db: Database) : UserPickRepository {
    object UserPickTable : Table("user_picks") {
        val userId = uuid("user_id")
        val tradeDate = date("trade_date")
        val pickId = integer("pick_id").nullable()
        val pickTime = timestamp("pick_time")
    }

    override suspend fun getUserPick(userId: String, tradeDate: LocalDate): Int? = newSuspendedTransaction(db = db) {
        val uuid = UUID.fromString(userId)
        UserPickTable.select {
            (UserPickTable.userId eq uuid) and (UserPickTable.tradeDate eq tradeDate)
        }
            .map { it[UserPickTable.pickId] }
            .singleOrNull()
    }

    override suspend fun upsertUserPick(userId: String, tradeDate: LocalDate, pick: Int?): Int? =
        newSuspendedTransaction(db = db) {
            val uuid = UUID.fromString(userId)
            val now = Instant.now()

            val exists =
                UserPickTable.select { (UserPickTable.userId eq uuid) and (UserPickTable.tradeDate eq tradeDate) }
                    .count() > 0

            if (exists) {
                UserPickTable.update({ (UserPickTable.userId eq uuid) and (UserPickTable.tradeDate eq tradeDate) }) {
                    it[UserPickTable.pickId] = pick
                    it[UserPickTable.pickTime] = now
                }
            } else {
                UserPickTable.insert {
                    it[UserPickTable.userId] = uuid
                    it[UserPickTable.tradeDate] = tradeDate
                    it[UserPickTable.pickId] = pick
                    it[UserPickTable.pickTime] = now
                }
            }
            pick
        }
}