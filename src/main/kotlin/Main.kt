package org.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.login.verifyIdToken
import org.example.model.http.LeaderBoard
import org.example.model.http.LeaderBoardUser
import org.example.model.http.User
import org.example.model.http.UserDetails
import org.example.repository.TradingSessionRepository
import org.example.service.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import java.time.LocalDate

fun main() {
    embeddedServer(
        Netty,
        port = 8080
    ) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(CallLogging) {
        level = org.slf4j.event.Level.INFO
    }
    install(ContentNegotiation) {
        json()
    }
    install(Koin) {
        modules(appModule)
    }
    install(ForwardedHeaders)
    install(CORS) {
        allowHost("localhost:3000")
        allowHost("tactx-api-61597259690.us-central1.run.app", listOf("https"))
        allowHost("hundredbucks.app", listOf("https"))
        allowCredentials = true  // if you need to send cookies/auth headers
        allowNonSimpleContentTypes = true

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
        val tradingSessionRepository by inject<TradingSessionRepository>()

        route("/api") {

            suspend fun withUserSessionCheck(
                call: ApplicationCall,
                followup: suspend (call: ApplicationCall, user: User) -> Unit
            ) {
                val sessionToken = call.request.cookies["hundred_bucks_session"]
                val user = sessionToken?.let {
                    userSessionService.getUserBySession(sessionToken)
                }
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

            get("/leaderboard") {
                val previousTradeDate = tradingSessionRepository.getPreviousTradeDate()
                val previousStocks =
                    previousTradeDate?.let { stockService.getStocks(it.tradeDate) }?.associateBy { it.id }
                val top10LeaderBoardUsers = userBalanceService.getCurrentTopUserBalances(10).map { userBalance ->
                    val userId = userBalance.userId.toString();
                    val user = userService.getUserById(userId)!!
                    val previousPick = previousTradeDate?.let { previousDate ->
                        userPickService.getUserPick(
                            userId,
                            previousDate.tradeDate
                        )
                    }
                    LeaderBoardUser(
                        user.userName,
                        userBalance.balance.toDouble(),
                        previousPick?.pickId?.let { pick -> previousStocks!![pick]!!.ticker },
                        previousTradeDate?.let { previousDate ->
                            userBalanceService.getHistoricUserBalance(
                                userId,
                                previousDate.tradeDate
                            )
                        }?.let
                        { previousBalance ->
                            userBalance.balance.minus(previousBalance.balance)
                        }?.toDouble() ?: 0.00
                    )
                }
                call.respond(LeaderBoard(top10LeaderBoardUsers))
            }

            put("/user/rename") {
                withUserSessionCheck(call) { call, user ->
                    call.respond(userService.updateUsername(user.userId, call.request.queryParameters["to"]!!))
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
                    val tradeDate = LocalDate.parse(call.parameters["tradeDate"]);
                    val currentSession = tradingSessionRepository.getCurrentTradingSession();
                    if (currentSession.tradeDate == tradeDate && currentSession.tradeable) {
                        call.respondNullable(
                            userPickService.updateUserPick(
                                user.userId,
                                LocalDate.parse(call.parameters["tradeDate"]),
                                Integer.parseInt(call.parameters["pickId"])
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }

            delete("/pick/{tradeDate}") {
                val tradeDate = LocalDate.parse(call.parameters["tradeDate"]);
                val currentSession = tradingSessionRepository.getCurrentTradingSession();
                withUserSessionCheck(call) { call, user ->
                    if (currentSession.tradeDate == tradeDate && currentSession.tradeable) {
                        call.respondNullable(
                            userPickService.updateUserPick(
                                user.userId,
                                LocalDate.parse(call.parameters["tradeDate"]),
                                null
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }

            get("/stocks/current") {
                call.respond(stockService.getDayStockView())
            }

            post("/auth/google") {
                call.application.environment.log.info("yoyo 1")
                val payload = call.receive<Map<String, String>>()
                val idToken = payload["googleOauthCredential"] ?: return@post call.respond(HttpStatusCode.BadRequest)

                call.application.environment.log.info("yoyo 2")
                val verifiedPayload = verifyIdToken(idToken)
                call.application.environment.log.info("yoyo 3")
                if (verifiedPayload == null) {
                    return@post call.respond(HttpStatusCode.Unauthorized, "Invalid ID token")
                }

                call.application.environment.log.info("yoyo 4")
                val email = verifiedPayload.email
                if (email == null) {
                    return@post call.respond(HttpStatusCode.BadRequest, "Email not found in ID token")
                }

                call.application.environment.log.info("yoyo 5")

                val sessionToken = userSessionService.loginUser(email);

                call.application.environment.log.info("yoyo 6")
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

                call.application.environment.log.info("yoyo 7")
                call.respond(HttpStatusCode.OK, mapOf("message" to "Logged in"))
            }
        }

        static("/") {
            resources("static") // serve files from resources/static folder
            defaultResource("index.html", "static") // serve index.html for SPA routing
        }
    }
}