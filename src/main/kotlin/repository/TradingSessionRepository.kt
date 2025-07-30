package org.example.repository

import org.example.model.db.TradingSession

interface TradingSessionRepository {
    suspend fun getCurrentTradingSession(): TradingSession
}