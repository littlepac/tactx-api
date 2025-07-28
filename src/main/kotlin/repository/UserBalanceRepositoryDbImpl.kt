package org.example.repository

import org.example.model.db.UserBalance
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class UserBalanceRepositoryDbImpl(private val db: Database) : UserBalanceRepository {

    object UserBalanceTable : Table("user_balance") {
        val userId = uuid("user_id")
        val forTradeDate = date("for_trade_date")
        val balance = decimal("balance", 15,4)
    }

    override suspend fun initBalance(
        userId: String,
        forTradingDate: LocalDate,
        balance: BigDecimal
    ) {
        newSuspendedTransaction(db = db) {
                UserBalanceTable.insert {
                    it[UserBalanceTable.userId] = UUID.fromString(userId)
                    it[UserBalanceTable.forTradeDate] = forTradingDate
                    it[UserBalanceTable.balance] = balance
                }
        }
    }

    override suspend fun getCurrentBalance(userId: String): UserBalance = newSuspendedTransaction(db = db) {
        val uuidUserId = UUID.fromString(userId)
        newSuspendedTransaction(db = db) {
            UserBalanceTable.select { UserBalanceTable.userId eq uuidUserId }.map {
                UserBalance(
                    it[UserBalanceTable.forTradeDate],
                    it[UserBalanceTable.balance]
                )
            }.singleOrNull() ?: throw NoSuchElementException("No balance found for userId: $userId")
        }
    }

}