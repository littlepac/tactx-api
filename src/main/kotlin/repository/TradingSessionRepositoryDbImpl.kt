package org.example.repository

import org.example.model.db.TradingSession
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDate

class TradingSessionRepositoryDbImpl(private val db: Database) : TradingSessionRepository {

    object TradingSessionTable : Table("trading_sessions") {
        val tradeDate = date("trade_date")
        val active = bool("active")
        val tradeable = bool("tradeable")
    }

    override suspend fun getCurrentTradingSession(): TradingSession = newSuspendedTransaction(db = db) {
        TradingSessionTable
            .select { TradingSessionTable.active eq true }
            .orderBy(TradingSessionTable.tradeDate to SortOrder.ASC)
            .limit(1)
            .map {
                TradingSession(it[TradingSessionTable.tradeDate], it[TradingSessionTable.tradeable])
            }
            .singleOrNull()
            ?: throw NoSuchElementException("No active trading session found")
    }
}