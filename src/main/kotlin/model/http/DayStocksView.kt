package org.example.model.http

import kotlinx.serialization.Serializable
import org.example.model.http.model.http.StockPickComment

@Serializable
data class DayStocksView(
    val tradeDate: String,
    val stocks: List<Stock>,
    val tradeable: Boolean,
    val comments: List<StockPickComment>
)