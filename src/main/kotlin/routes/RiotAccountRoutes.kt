package routes

import config.RiotRegion
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import schemas.RiotAccountResponse
import services.RiotAccountService

fun Route.accountRoutes() {
    route("/account") {
        get("/") {
            call.respondText("Account Index")
        }

        // 닉네임 기반 puuid 조회
        get("/by-riot-id/{gameName}/{tagLine}") {
            val gameName = call.parameters["gameName"] ?: ""        // 닉네임
            val tagLine = call.parameters["tagLine"] ?: ""          // 태그명

            // Riot 엔트포인트 요청
            val result = RiotAccountService.getByRiotId(RiotRegion.ASIA, gameName, tagLine)

            call.respond(result)
        }


    }
}