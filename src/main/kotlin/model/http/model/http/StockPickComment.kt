package org.example.model.http.model.http

import kotlinx.serialization.Serializable

@Serializable
data class StockPickComment(
    val id: Int?,
    val comment: String?
)