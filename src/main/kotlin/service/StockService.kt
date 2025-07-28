package org.example.service

import org.example.model.http.DayStocksView
import org.example.model.http.Stock
import org.example.repository.PickRepository
import java.math.BigDecimal

class StockService(private val currentDateService: CurrentDateService, private val pickRepository: PickRepository) {

    suspend fun getDayStockView(): DayStocksView {
        val date = currentDateService.getCurrentDate()
        val stocks = pickRepository.getCurrentStocks(date)
        return DayStocksView(
            date.toString(),
            stocks.map { stock ->
                Stock(
                    stock.id,
                    stock.ticker,
                    stock.previousClose.divide(stock.previousOpen).minus(BigDecimal.ONE).toDouble(),
                    stock.pickReason
                )
            }
        )
    }
}