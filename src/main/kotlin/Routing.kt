package com.example

import com.example.routes.championRoutes
import com.example.routes.summonerRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.*
import routes.matchRoutes

fun Application.configureRouting() {
    routing {
        summonerRoutes()
        championRoutes()
        matchRoutes()

        get("/") {
            call.respondText("Hello World!")
        }
    }
}
