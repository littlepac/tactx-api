package org.example.model.http

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val id: Int,
    val ticker: String,
    val name: String,
    val previousOpen: Double,
    val previousClose: Double,
    val reasonForSelection: String
)