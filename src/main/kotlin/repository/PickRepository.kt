package org.example.repository

import org.example.model.db.Stock
import java.time.LocalDate

interface PickRepository {
    suspend fun getStocks(tradeDate: LocalDate): List<Stock>
}