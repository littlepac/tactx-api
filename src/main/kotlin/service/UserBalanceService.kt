package org.example.service

import org.example.model.db.UserBalance
import org.example.repository.TradingSessionRepository
import org.example.repository.UserBalanceRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.NoSuchElementException

class UserBalanceService(
    private val userBalanceRepository: UserBalanceRepository,
    private val tradingSessionRepository: TradingSessionRepository
) {
    private val defaultBalance : BigDecimal =  BigDecimal("100");

    suspend fun getUserBalance(userId: String): UserBalance {
        return userBalanceRepository.findBalanceByDate(userId, tradingSessionRepository.getCurrentTradingSession().tradeDate) ?: throw NoSuchElementException("No balance found for userId: $userId")
    }
    suspend fun getHistoricUserBalance(userId: String, tradeDate: LocalDate): UserBalance? {
        return userBalanceRepository.findBalanceByDate(userId, tradeDate)
    }

    suspend fun initUserBalance(userId: String) {
        userBalanceRepository.initBalance(userId, tradingSessionRepository.getCurrentTradingSession().tradeDate, defaultBalance)
    }

    suspend fun getCurrentTopUserBalances(limit: Int): List<UserBalance> {
        return userBalanceRepository.getTopUserBalances(
            tradingSessionRepository.getCurrentTradingSession().tradeDate,
            limit
        )
    }
}