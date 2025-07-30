package org.example.service

import org.example.model.http.DayStocksView
import org.example.model.http.Stock
import org.example.repository.PickRepository
import org.example.repository.TradingSessionRepository
import java.math.BigDecimal
import java.math.RoundingMode

class StockService(
    private val tradingSessionRepository: TradingSessionRepository,
    private val pickRepository: PickRepository
) {

    suspend fun getDayStockView(): DayStocksView {
        val currentSession = tradingSessionRepository.getCurrentTradingSession()
        val date = currentSession.currentTradeDate
        val stocks = pickRepository.getCurrentStocks(date)
        return DayStocksView(
            date.toString(),
            stocks.map { stock ->
                Stock(
                    stock.id,
                    stock.ticker,
                    stock.previousClose
                        .divide(stock.previousOpen, 4, RoundingMode.HALF_UP)
                        .minus(BigDecimal.ONE).toDouble(),
                    stock.pickReason
                )
            },
            currentSession.tradeable
        )
    }
}