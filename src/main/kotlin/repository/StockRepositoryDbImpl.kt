package org.example.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class StockRepositoryDbImpl(private val db: Database) : StockRepository {
    object StockTable : Table("stocks") {
        val ticker = varchar("ticker", 10)
        val name = varchar("name", 255)
    }

    override suspend fun getStockNameByTicker(ticker: String): String {
        return newSuspendedTransaction(db = db) {
            StockTable.select { StockTable.ticker.eq(ticker) }.map { it[StockTable.name] }.single()
        }
    }
}