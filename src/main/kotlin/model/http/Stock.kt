package org.example.model.http

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val id: Int,
    val ticker: String,
    val previousDayMove: Double,
    val reasonForSelection: String
)