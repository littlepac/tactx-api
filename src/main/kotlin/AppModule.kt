package org.example

import org.example.repository.UserRepository
import org.example.repository.UserRepositoryDbImpl
import org.example.service.UserService
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryDbImpl() }
    single { UserService(get()) }
}