package org.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.service.UserService
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }

        install(Koin) {
            slf4jLogger()
            modules(appModule)
        }

        routing {
            val userService by inject<UserService>()

            get("/user/{id}") {
                val idParam = call.parameters["id"]
                if (idParam == null) {
                    call.respond("Missing or invalid user id")
                    return@get
                }
                val user = userService.findUser(idParam)
                if (user == null) call.respond("User not found") else call.respond(user)
            }
        }
    }.start(wait = true)
}