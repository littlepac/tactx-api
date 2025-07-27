package org.example.repository

import org.example.model.db.UserBalance
import java.math.BigDecimal
import java.time.LocalDate

interface UserBalanceRepository {
    suspend fun initBalance(userId: String, forTradingDate: LocalDate, balance: BigDecimal)
    suspend fun getCurrentBalance(userId: String) : UserBalance
}