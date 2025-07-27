package org.example.repository

import org.example.model.db.Stock
import org.example.model.db.UserBalance
import java.math.BigDecimal
import java.time.LocalDate

class StockRepositoryDbImpl : StockRepository {
    override suspend fun getCurrentStocks(tradeDate: LocalDate): List<Stock> {
        return listOf(
            Stock(0, "NVDA", BigDecimal.ONE, BigDecimal.TEN,"The best stock"),
            Stock(1, "AAPL", BigDecimal.ONE, BigDecimal.TEN, "The 2nd best stock"),
            Stock(2, "MSFT", BigDecimal.ONE, BigDecimal.TEN, "The 3rd best stock"),
            Stock(3, "TSLA", BigDecimal.ONE, BigDecimal.TEN, "The 4th best stock"),
        )
    }

}