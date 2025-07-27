package org.example.repository

import java.time.LocalDate

interface UserPickRepository {
    suspend fun getUserPick(userId: String, tradeDate: LocalDate): Int?

    suspend fun upsertUserPick(userId: String, tradeDate: LocalDate, pick: Int?): Int?
}