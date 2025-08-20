package org.example.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDate

class PickCommentsRepositoryDbImpl(private val db: Database) : PickCommentsRepository {
    object PickCommentsTable : Table("pick_comments") {
        val tradeDate = date("trade_date")
        val pickId = integer("pick_id").nullable()
        val comment = text("comment").nullable()
    }

    override suspend fun getPickComment(tradeDate: LocalDate, pickId: Int?): String? {
        return newSuspendedTransaction(db = db) {
            PickCommentsTable
                .select { PickCommentsTable.tradeDate.eq(tradeDate) and PickCommentsTable.pickId.eq(pickId) }
                .map { it[PickCommentsTable.comment] }.firstOrNull()
        }
    }
}