package org.example.model.http

import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val userName: String,
    val currentAccountBalance: Double,
    val updatedTradeDate: String
)