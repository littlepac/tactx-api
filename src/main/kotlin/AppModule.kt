package org.example

import org.example.repository.StockRepository
import org.example.repository.StockRepositoryDbImpl
import org.example.repository.UserBalanceRepository
import org.example.repository.UserBalanceRepositoryDbImpl
import org.example.repository.UserPickRepository
import org.example.repository.UserPickRepositoryDbImpl
import org.example.repository.UserSessionRepository
import org.example.repository.UserSessionRepositoryDbImpl
import org.example.repository.UserRepository
import org.example.repository.UserRepositoryDbImpl
import org.example.service.CurrentDateService
import org.example.service.StockService
import org.example.service.UserBalanceService
import org.example.service.UserPickService
import org.example.service.UserSessionService
import org.example.service.UserService
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val appModule = module {
    single<Database> {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/postgres",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "postgres"
        )
    }
    single<UserRepository> { UserRepositoryDbImpl(get()) }
    single<UserSessionRepository> { UserSessionRepositoryDbImpl(get()) }
    single<UserPickRepository> { UserPickRepositoryDbImpl() }
    single<StockRepository> { StockRepositoryDbImpl() }
    single<UserBalanceRepository> { UserBalanceRepositoryDbImpl() }
    single { UserService(get(), get()) }
    single { UserSessionService(get(), get()) }
    single { StockService(get(), get()) }
    single { UserPickService(get()) }
    single { CurrentDateService() }
    single { UserBalanceService(get(),get()) }

}