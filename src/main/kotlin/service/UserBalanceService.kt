package org.example.service

import org.example.model.db.UserBalance
import org.example.repository.UserBalanceRepository
import java.math.BigDecimal

class UserBalanceService(
    private val userBalanceRepository: UserBalanceRepository,
    private val currentDateService: CurrentDateService
) {
    private val defaultBalance : BigDecimal =  BigDecimal("100");

    suspend fun getUserBalance(userId: String): UserBalance {
        return userBalanceRepository.getCurrentBalance(userId)
    }

    suspend fun initUserBalance(userId: String) {
        userBalanceRepository.initBalance(userId, currentDateService.getCurrentDate(), defaultBalance)
    }
}