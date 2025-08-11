package org.example.model.http

import kotlinx.serialization.Serializable

@Serializable
data class LeaderBoard(
    val top10: List<LeaderBoardUser>
)

@Serializable
data class LeaderBoardUser(val userName: String, val currentBalance: Double, val lastTradedTicker: String?, val lastIncrement: Double)