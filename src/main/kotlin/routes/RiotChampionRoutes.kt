package routes

import config.DataDragonUrl
import config.RiotLanguage
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.response.respond
import io.ktor.server.thymeleaf.ThymeleafContent
import services.RiotChampionService

fun Route.championRoutes() {
    val ddVersion = "15.18.1"

    route("/champion") {
        get("/") {
            call.respondText("Champion Index")
        }

        get("/champions") {
            val champions = RiotChampionService.getChampionSummaryInfo(
                params = mapOf(
                    "version" to ddVersion,
                    "language" to RiotLanguage.KO_KR.code
                )
            )

            val model = mapOf(
                "champions" to champions,
                "version" to ddVersion
            )

            call.respond(ThymeleafContent("champion/champion-list", model))
        }

        get("/{id}") {
            val championId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "id required")
            val model = RiotChampionService.getChampionDetailInfo(ddVersion, RiotLanguage.KO_KR.code, championId)

            call.respond(ThymeleafContent("champion/detail", model))
        }

    }
}