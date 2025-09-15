package routes

import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.matchRoutes() {
    route("/match") {
        get("/") {
            call.respondText("Match Index")
        }
    }


}