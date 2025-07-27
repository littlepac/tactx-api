package org.example.repository

import org.example.model.db.UserBalance
import java.math.BigDecimal
import java.time.LocalDate

class UserBalanceRepositoryDbImpl : UserBalanceRepository {

    override suspend fun initBalance(
        userId: String,
        forTradingDate: LocalDate,
        balance: BigDecimal
    ) {

    }

    override suspend fun getCurrentBalance(userId: String): UserBalance {
        TODO()
    }

}