package org.example.model.db

import java.math.BigDecimal
import java.time.LocalDate

data class UserBalance(
    val forTradeDate: LocalDate,
    val balance: BigDecimal
)