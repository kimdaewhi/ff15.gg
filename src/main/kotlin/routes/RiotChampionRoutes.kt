package routes

import DTO.*
import config.DataDragonUrl
import config.RiotLanguage
import config.buildDataDragonUrl
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.response.respond
import io.ktor.server.thymeleaf.ThymeleafContent
import kotlinx.serialization.json.Json
import services.RiotChampionService
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.*

fun Route.championRoutes() {
    val ddVersion = "15.18.1"

    route("/champion") {
        get("/") {
            call.respondText("Champion Index")
        }

        get("/champions") {
            val champions = RiotChampionService.getChampionSummaryInfo(
                ddUrl = DataDragonUrl.CHAMPION_LIST_INFO,
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
            val rawId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "id required")
            println("rawId : $rawId")

            // 1) 로컬 샘플 읽기
            val base = Paths.get("C:\\Users\\kimda\\Downloads\\")
            val catalogJson = Files.readString(base.resolve("champions_sample.json"))
            val catalog = Json.decodeFromString(RiotChampionCatalogDto.serializer(), catalogJson)

            // id로 찾기(Champion.id 또는 한글 name 둘 다 허용)
            val byId = catalog.data.values.firstOrNull { it.id.equals(rawId, ignoreCase = true) }
            val champForHeader = byId ?: catalog.data.values.firstOrNull { it.name == rawId }
            ?: return@get call.respond(HttpStatusCode.NotFound, "champion not found")

            // 상세(지금은 jinx 샘플만 있으니 id가 Jinx일 때만 파일 사용)
            val detailFile =
                if (champForHeader.id.equals("Jinx", ignoreCase = true))
                    base.resolve("champion_sample_jinx.json")
                else null

            val (detail, ddVersion) = if (detailFile != null && Files.exists(detailFile)) {
                val detailJson = Files.readString(detailFile)
                val detailCatalog = Json.decodeFromString(RiotChampionDetailCatalogDto.serializer(), detailJson)
                val d = detailCatalog.data.values.first()
                d to detailCatalog.version
            } else {
                // 상세 JSON이 없으면 헤더용 데이터로 최소 모델 구성
                RiotChampionDetailDto(
                    id = champForHeader.id,
                    key = champForHeader.key,
                    name = champForHeader.name,
                    title = champForHeader.title,
                    image = champForHeader.image,
                    info = champForHeader.info,
                    stats = champForHeader.stats
                ) to (champForHeader.version)
            }

            // 2) 이미지 URL들 생성
            val lang = "ko_KR"
            val champIconUrl = config.buildDataDragonUrl(
                ddurl = DataDragonUrl.CHAMPION_ICON_ASSET,
                params = mapOf(
                    "version" to ddVersion,
                    "language" to RiotLanguage.KO_KR.code,
                    "championId" to champForHeader.id
                )
            )

            val passiveIconUrl = detail.passive?.image?.full?.let { full ->
                buildDataDragonUrl(
                    ddurl = DataDragonUrl.CHAMPION_PASSIVE_ASSET,
                    params = mapOf(
                        "version" to ddVersion,
                        "language" to RiotLanguage.KO_KR.code,
                        "championPassiveId" to full
                    )
                )
            }

            val spellViews = detail.spells.map { sp ->
                val icon = buildDataDragonUrl(
                    ddurl = DataDragonUrl.CHAMPION_SPELL_ASSET,
                    params = mapOf(
                        "version" to ddVersion,
                        "language" to RiotLanguage.KO_KR.code,
                        "championSpellId" to (sp.image?.full ?: "")
                    )
                )
                mapOf(
                    "id" to sp.id,
                    "name" to sp.name,
                    // tooltip(HTML 포함) 있으면 utext로 렌더, 없으면 description 사용
                    "tooltip" to (sp.tooltip ?: sp.description),
                    "icon" to icon
                )
            }

            val skinViews = detail.skins.map { skin ->
                val splash = buildDataDragonUrl(
                    ddurl = DataDragonUrl.CHAMPION_SPLASH_ASSET,
                    params = mapOf(
                        "version" to ddVersion,
                        "language" to RiotLanguage.KO_KR.code,
                        "champoinId" to detail.id, "num" to skin.num.toString()
                    )
                )
                mapOf("name" to (if (skin.name == "default") "기본 스킨" else skin.name), "splash" to splash)
            }

            // 3) 모델
            val model = mapOf(
                "v" to ddVersion,
                "iconUrl" to champIconUrl,
                "champ" to detail,
                "passiveIcon" to passiveIconUrl,
                "spells" to spellViews,
                "skins" to skinViews
            )

            println(model)

            call.respond(ThymeleafContent("champion/detail", model))
        }

    }
}