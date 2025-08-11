package org.example.model.db

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class UserBalance(
    val userId: UUID,
    val forTradeDate: LocalDate,
    val balance: BigDecimal
)