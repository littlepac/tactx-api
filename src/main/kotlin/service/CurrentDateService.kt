package org.example.service

import java.time.LocalDate

class CurrentDateService() {
    fun getCurrentDate(): LocalDate {
        return LocalDate.now()
    }
}