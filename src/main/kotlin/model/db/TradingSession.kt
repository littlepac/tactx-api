package org.example.model.db

import java.time.LocalDate

data class TradingSession(
    val currentTradeDate: LocalDate,
    val tradeable: Boolean
)