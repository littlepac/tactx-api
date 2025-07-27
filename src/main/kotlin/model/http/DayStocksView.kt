package org.example.model.http

import kotlinx.serialization.Serializable

@Serializable
data class DayStocksView(
    val tradeDate: String,
    val stocks: List<Stock>
)