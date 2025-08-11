package org.example.model.db

import java.time.LocalDate

data class TradingSession(
    val tradeDate: LocalDate,
    val tradeable: Boolean
)