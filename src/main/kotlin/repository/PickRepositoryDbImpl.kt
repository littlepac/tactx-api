package org.example.repository

import org.example.model.db.Stock
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDate

class PickRepositoryDbImpl(private val db: Database) : PickRepository {

    object PickTable : Table("picks") {
        val tradeDate = date("trade_date")
        val pickId = integer("pick_id")
        val ticker = varchar("ticker", 10)
        val previousOpen = decimal("previous_open", 10, 4)
        val previousClose = decimal("previous_close", 10, 4)
        val pickReason = text("pick_reason")
        val finalOpen = decimal("final_open", 10, 4).nullable()
        val finalClose = decimal("final_close", 10, 4).nullable()
    }

    override suspend fun getStocks(tradeDate: LocalDate): List<Stock> = newSuspendedTransaction(db = db) {
        PickTable.select { PickTable.tradeDate eq tradeDate }
            .map { row ->
                Stock(
                    id = row[PickTable.pickId],
                    ticker = row[PickTable.ticker],
                    previousOpen = row[PickTable.previousOpen],
                    previousClose = row[PickTable.previousClose],
                    pickReason = row[PickTable.pickReason],
                    finalOpen = row[PickTable.finalOpen],
                    finalClose = row[PickTable.finalClose],
                )
            }
    }

}