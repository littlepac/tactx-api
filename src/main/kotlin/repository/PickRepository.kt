package org.example.repository

import org.example.model.db.Stock
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

interface PickRepository {
    suspend fun getCurrentStocks(tradeDate: LocalDate): List<Stock>
}