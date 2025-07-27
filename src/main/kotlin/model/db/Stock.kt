package org.example.model.db

import java.math.BigDecimal

data class Stock(
    val id: Int,
    val ticker: String,
    val previousOpen: BigDecimal,
    val previousClose: BigDecimal,
    val reasonForSelection: String,
)