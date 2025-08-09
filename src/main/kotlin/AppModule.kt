package org.example

import org.example.repository.PickRepository
import org.example.repository.PickRepositoryDbImpl
import org.example.repository.StockRepository
import org.example.repository.StockRepositoryDbImpl
import org.example.repository.TradingSessionRepository
import org.example.repository.TradingSessionRepositoryDbImpl
import org.example.repository.UserBalanceRepository
import org.example.repository.UserBalanceRepositoryDbImpl
import org.example.repository.UserPickRepository
import org.example.repository.UserPickRepositoryDbImpl
import org.example.repository.UserSessionRepository
import org.example.repository.UserSessionRepositoryDbImpl
import org.example.repository.UserRepository
import org.example.repository.UserRepositoryDbImpl
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
            "jdbc:postgresql://localhost:5432/dbname?socketFactory=com.google.cloud.sql.postgres.SocketFactory&cloudSqlInstance=hundred-bucks-467005:us-central1:hundred-bucks-dev",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "100Bucks$"
        )

//        Database.connect(
//            url = "jdbc:postgresql://localhost:5432/postgres",
//            driver = "org.postgresql.Driver",
//            user = "postgres",
//            password = "postgres"
//        )
    }
    single<UserRepository> { UserRepositoryDbImpl(get()) }
    single<UserSessionRepository> { UserSessionRepositoryDbImpl(get()) }
    single<UserPickRepository> { UserPickRepositoryDbImpl(get()) }
    single<PickRepository> { PickRepositoryDbImpl(get()) }
    single<UserBalanceRepository> { UserBalanceRepositoryDbImpl(get()) }
    single<TradingSessionRepository> { TradingSessionRepositoryDbImpl(get()) }
    single<StockRepository> { StockRepositoryDbImpl(get()) }
    single { UserService(get(), get()) }
    single { UserSessionService(get(), get()) }
    single { StockService(get(), get(), get()) }
    single { UserPickService(get()) }
    single { UserBalanceService(get(),get()) }

}