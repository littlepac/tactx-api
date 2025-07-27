package org.example.repository

import org.example.model.db.Stock
import java.time.LocalDate

interface StockRepository {
    suspend fun getCurrentStocks(tradeDate: LocalDate): List<Stock>
}