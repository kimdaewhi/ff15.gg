package routes

import config.RiotEndpointUrl
import config.RiotPlatform
import config.RiotRegion
import config.DataDragonUrl
import config.RiotLanguage
import config.buildDataDragonUrl
import kotlin.time.Duration.Companion.milliseconds
import io.ktor.http.HttpStatusCode
import io.ktor.http.buildUrl
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.thymeleaf.ThymeleafContent
import schemas.*
import services.*
import utils.humanizeAgo
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun Route.accountRoutes() {
    route("/account") {
        get("/") {
            call.respondText("Account Index")
        }

        // ❌ 닉네임 기반 puuid 조회(사용 x)
        get("/get-by-riotid/{gameName}/{tagLine}") {
            val gameName = call.parameters["gameName"] ?: ""        // 닉네임
            val tagLine = call.parameters["tagLine"] ?: ""          // 태그명

            // Riot 엔트포인트 요청
            val result = RiotAccountService.getAccountInfoByRiotID(RiotRegion.ASIA, gameName, tagLine)

            call.respond(result)
        }


        // 닉네임 기반 소환사 정보 조회
        get ("/get-summoner-info/{gameName}/{tagLine}") {
            val gameName = call.parameters["gameName"] ?: return@get call.respondText("gameName required", status = HttpStatusCode.BadRequest)
            val tagLine  = call.parameters["tagLine"]  ?: return@get call.respondText("tagLine required",  status = HttpStatusCode.BadRequest)

            // 닉네임#태그명으로 소환사 puuid 구하기
            val p = RiotAccountService.getSummonerInfoByRiotID(
                region   = RiotRegion.ASIA,
                platform = RiotPlatform.KR,
                gameName = gameName,
                tagLine  = tagLine
            )

            // DDragon 버전 임시로 고정, 소환사 프로필 아이콘 url 생성
            val ddVersion = "15.18.1"
            val profileIconUrl = buildDataDragonUrl(
                ddurl=DataDragonUrl.PROFILE_ICON_ASSET,
                params= mapOf(
                    "version" to ddVersion,
                    "language" to RiotLanguage.KO_KR.code,
                    "profileIconId" to p.profileIconId.toString()
                )
            )

            // 새로고침 url 만들어서 같이 넘기기
            val refreshUrl =
                "/account/get-summoner-info/${
                    URLEncoder.encode(p.gameName, StandardCharsets.UTF_8)
                }/${
                    URLEncoder.encode(p.tagLine, StandardCharsets.UTF_8)
                }"

            // thymeleaf template parameter로 넘길 map 생성
            val model = mapOf(
                "p" to p,
                "profileIconUrl" to profileIconUrl,
                "lastUpdated" to humanizeAgo(p.revisionDate),
                // 선택: 시즌/티어 배지 예시
                "badges" to listOf("2024-2 Bronze 1", "2021 Gold 4"),
                "refreshUrl" to refreshUrl,
            )
            println(model)
            call.respond(ThymeleafContent("summoner/profile", model))
        }
    }
}