package org.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.login.verifyIdToken
import org.example.model.http.User
import org.example.model.http.UserDetails
import org.example.service.StockService
import org.example.service.UserBalanceService
import org.example.service.UserPickService
import org.example.service.UserService
import org.example.service.UserSessionService
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.security.KeyStore
import java.time.LocalDate

fun main() {
    val keyStore = KeyStore.getInstance("JKS").apply {
        val resourceStream = object {}.javaClass.classLoader.getResourceAsStream("keystore.jks")
            ?: throw RuntimeException("Keystore not found")
        load(resourceStream, "passw0rd".toCharArray()) // your keystore password
    }

    embeddedServer(
        Netty,
        environment = applicationEngineEnvironment {
            module {
                // Your module code (same as your current code)
                module()
            }
            sslConnector(
                keyStore = keyStore,
                keyAlias = "tactx-ssl",
                keyStorePassword = { "passw0rd".toCharArray() }, // keystore password
                privateKeyPassword = { "passw0rd".toCharArray() }) {
                host = "localhost"
                port = 8443 // Standard HTTPS port
            }
        }   // key password you chose during generation
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
    install(CORS) {
        allowHost("localhost:3000")
        allowCredentials = true  // if you need to send cookies/auth headers
        allowNonSimpleContentTypes = true

        // Allow common HTTP methods for your API
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)

        // Allow typical headers sent from frontend
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.Origin)
    }

    routing {
        val userSessionService by inject<UserSessionService>()
        val userBalanceService by inject<UserBalanceService>()
        val userPickService by inject<UserPickService>()
        val stockService by inject<StockService>()
        val userService by inject<UserService>()

        route("/api") {

            suspend fun withUserSessionCheck(call: ApplicationCall, followup: suspend (call: ApplicationCall, user: User) -> Unit) {
                val sessionToken = call.request.cookies["hundred_bucks_session"]
                val user = sessionToken?.let {
                    userSessionService.getUserBySession(sessionToken)
                } ?: userService.getUserByEmail("tkpc071083@gmail.com")
                if (user != null) {
                    followup(call, user);
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

            get("/user/details") {
                withUserSessionCheck(call) { call, user ->
                    val userBalance = userBalanceService.getUserBalance(user.userId)
                    val userDetails = UserDetails(
                        user.userName,
                        userBalance.balance.toDouble(),
                        userBalance.forTradeDate.toString()
                    )
                    call.respond(userDetails)
                }
            }

            get("/pick/{tradeDate}") {
                withUserSessionCheck(call) { call, user ->
                    val pick = userPickService.getUserPick(user.userId, LocalDate.parse(call.parameters["tradeDate"]))
                    call.respondNullable(pick)
                }
            }

            put("/pick/{tradeDate}/{pickId}") {
                withUserSessionCheck(call) { call, user ->
                    call.respondNullable(userPickService.updateUserPick(user.userId, LocalDate.parse(call.parameters["tradeDate"]), Integer.parseInt(call.parameters["pickId"])))
                }
            }

            delete("/pick/{tradeDate}") {
                withUserSessionCheck(call) { call, user ->
                    call.respondNullable(userPickService.updateUserPick(user.userId, LocalDate.parse(call.parameters["tradeDate"]), null))
                }
            }

            get("/stocks/current") {
                call.respond(stockService.getDayStockView())
            }

            post("/auth/google") {
                val payload = call.receive<Map<String, String>>()
                val idToken = payload["googleOauthCredential"] ?: return@post call.respond(HttpStatusCode.BadRequest)

                val verifiedPayload = verifyIdToken(idToken)
                if (verifiedPayload == null) {
                    return@post call.respond(HttpStatusCode.Unauthorized, "Invalid ID token")
                }

                val email = verifiedPayload.email
                if (email == null) {
                    return@post call.respond(HttpStatusCode.BadRequest, "Email not found in ID token")
                }

                val sessionToken = userSessionService.loginUser(email);
                call.response.cookies.append(
                    Cookie(
                        name = "hundred_bucks_session",
                        value = sessionToken,
                        httpOnly = false,
                        secure = true,
                        maxAge = 60 * 60 * 24 * 365 * 10,  // 10 years
                        path = "/",
                    )
                )

                call.respond(HttpStatusCode.OK, mapOf("message" to "Logged in"))
            }
        }

        static("/") {
            resources("static") // serve files from resources/static folder
            defaultResource("index.html", "static") // serve index.html for SPA routing
        }
    }
}