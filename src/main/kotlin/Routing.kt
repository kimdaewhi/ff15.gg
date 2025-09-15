package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import routes.championRoutes
import routes.matchRoutes
import routes.accountRoutes

fun Application.configureRouting() {
    routing {
        accountRoutes()
        championRoutes()
        matchRoutes()

        get("/") {
            call.respondText("Hello World!")
        }
    }
}
