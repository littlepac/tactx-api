package org.example.repository

import org.example.model.db.TradingSession
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TradingSessionRepositoryDbImpl(private val db: Database) : TradingSessionRepository {

    object TradingSessionTable : Table("trading_sessions") {
        val tradeDate = date("trade_date")
        val active = bool("active")
        val tradeable = bool("tradeable")
    }

    override suspend fun getCurrentTradingSession(): TradingSession = newSuspendedTransaction(db = db) {
        TradingSessionTable
            .select { TradingSessionTable.active eq true }
            .map {
                TradingSession(it[TradingSessionTable.tradeDate], it[TradingSessionTable.tradeable])
            }
            .singleOrNull()
            ?: throw NoSuchElementException("No active trading session found")
    }

    override suspend fun getPreviousTradeDate(): TradingSession? {
        val currentTradingSession = getCurrentTradingSession()
        return newSuspendedTransaction(db = db) {
            TradingSessionTable
                .select { (TradingSessionTable.active eq false) and (TradingSessionTable.tradeDate less currentTradingSession.tradeDate) }
                .orderBy(TradingSessionTable.tradeDate to SortOrder.DESC)
                .limit(1)
                .map {
                    TradingSession(it[TradingSessionTable.tradeDate], it[TradingSessionTable.tradeable])
                }
                .singleOrNull()
        }
    }
}