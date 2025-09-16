package com.example

import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.thymeleaf.ThymeleafContent
import routes.championRoutes
import routes.matchRoutes
import routes.accountRoutes

fun Application.configureRouting() {

    routing {
        staticResources("/static", "static")

        accountRoutes()
        championRoutes()
        matchRoutes()

        get("/") {
            call.respond(
                ThymeleafContent(
                    template = "index",
                    model = mapOf("name" to "Ktor"))
            )
        }
    }
}
