package org.example.repository

import java.time.LocalDate

class UserPickRepositoryDbImpl : UserPickRepository {
    override suspend fun getUserPick(userId: String, tradeDate: LocalDate): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun upsertUserPick(
        userId: String,
        tradeDate: LocalDate,
        pick: Int?
    ): Int? {
        TODO("Not yet implemented")
    }

}