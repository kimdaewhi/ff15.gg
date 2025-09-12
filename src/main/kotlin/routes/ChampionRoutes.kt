package com.example.routes

import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.championRoutes() {
    route("/champion") {
        get("/") {
            call.respondText("Champion Index")
        }
    }
}