package org.example.repository

import org.example.model.db.UserBalance
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
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

    override suspend fun findBalanceByDate(userId: String, tradeDate: LocalDate): UserBalance? = newSuspendedTransaction(db = db) {
        val uuidUserId = UUID.fromString(userId)
        newSuspendedTransaction(db = db) {
            UserBalanceTable.select { (UserBalanceTable.userId eq uuidUserId) and (UserBalanceTable.forTradeDate eq tradeDate)}.map {
                UserBalance(
                    it[UserBalanceTable.userId],
                    it[UserBalanceTable.forTradeDate],
                    it[UserBalanceTable.balance]
                )
            }.singleOrNull()
        }
    }

    override suspend fun getTopUserBalances(tradeDate: LocalDate, limit: Int): List<UserBalance> = newSuspendedTransaction(db = db) {
        UserBalanceTable
            .select { UserBalanceTable.forTradeDate eq tradeDate}
            .orderBy(UserBalanceTable.balance, SortOrder.DESC)
            .limit(limit)
            .map {
                UserBalance(
                    it[UserBalanceTable.userId],
                    it[UserBalanceTable.forTradeDate],
                    it[UserBalanceTable.balance]
                )
            }
    }

}