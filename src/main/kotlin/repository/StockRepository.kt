package org.example.repository

interface StockRepository {
    suspend fun getStockNameByTicker(ticker: String): String
}