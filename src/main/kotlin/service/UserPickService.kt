package org.example.service

import org.example.model.db.UserPick
import org.example.repository.UserPickRepository
import java.time.LocalDate

class UserPickService(private val userPickRepository: UserPickRepository)  {
    suspend fun getUserPick(userId: String, tradeDate: LocalDate): UserPick? {
        return userPickRepository.getUserPick(userId, tradeDate);
    }

    suspend fun updateUserPick(userId: String, tradeDate: LocalDate, pick: Int?): UserPick? {
        return userPickRepository.upsertUserPick(userId, tradeDate, pick)
    }
}