package org.example.service

import org.example.model.http.DayStocksView
import org.example.model.http.Stock
import org.example.repository.PickRepository
import org.example.repository.StockRepository
import org.example.repository.TradingSessionRepository

class StockService(
    private val tradingSessionRepository: TradingSessionRepository,
    private val pickRepository: PickRepository,
    private val stockRepository: StockRepository
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
                    stockRepository.getStockNameByTicker(stock.ticker),
                    stock.previousOpen.toDouble(),
                    stock.previousClose.toDouble(),
                    stock.pickReason
                )
            },
            currentSession.tradeable
        )
    }
}