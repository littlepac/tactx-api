package org.example.repository

import java.time.LocalDate

interface PickCommentsRepository {
    suspend fun getPickComment(tradeDate: LocalDate, pickId: Int?): String?
}