package org.example.service

import org.example.model.http.DayStocksView
import org.example.model.http.Stock
import org.example.model.http.model.http.StockPickComment
import org.example.repository.PickCommentsRepository
import org.example.repository.PickRepository
import org.example.repository.StockRepository
import org.example.repository.TradingSessionRepository
import java.time.LocalDate

class StockService(
    private val tradingSessionRepository: TradingSessionRepository,
    private val pickRepository: PickRepository,
    private val stockRepository: StockRepository,
    private val pickCommentsRepository: PickCommentsRepository
) {
    suspend fun getDayStockView(): DayStocksView {
        val currentSession = tradingSessionRepository.getCurrentTradingSession()
        val date = currentSession.tradeDate
        val stocks = pickRepository.getStocks(date)
        val comments = mutableListOf<StockPickComment>();
        pickCommentsRepository.getPickComment(date,0)?.let { comments.add(StockPickComment(0, it)) }
        pickCommentsRepository.getPickComment(date,1)?.let { comments.add(StockPickComment(1, it)) }
        pickCommentsRepository.getPickComment(date,2)?.let { comments.add(StockPickComment(2, it)) }
        pickCommentsRepository.getPickComment(date,3)?.let { comments.add(StockPickComment(3, it)) }
        pickCommentsRepository.getPickComment(date, null)?.let { comments.add(StockPickComment(null, it)) }
        return DayStocksView(
            date.toString(),
            stocks.map { stock ->
                Stock(
                    stock.id,
                    stock.ticker,
                    stockRepository.getStockNameByTicker(stock.ticker),
                    stock.previousOpen.toDouble(),
                    stock.previousClose.toDouble(),
                    stock.pickReason,
                )
            },
            currentSession.tradeable,
            comments
        )
    }

    suspend fun getStocks(tradeDate: LocalDate): List<org.example.model.db.Stock> {
        return pickRepository.getStocks(tradeDate);
    }
}