package org.example.repository

import org.example.model.db.UserPick
import java.time.LocalDate

interface UserPickRepository {
    suspend fun getUserPick(userId: String, tradeDate: LocalDate): UserPick?
    suspend fun upsertUserPick(userId: String, tradeDate: LocalDate, pick: Int?): UserPick?
}