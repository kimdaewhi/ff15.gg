package services

import DTO.RiotChampionCatalogDto
import DTO.ChampionIcon
import DTO.RiotChampionDetailCatalogDto
import config.RiotEndpoints
import io.ktor.client.call.body
import io.ktor.client.request.get
import plugins.httpClient

object RiotChampionService {

    suspend fun getChampionCatalog(params: Map<String, String>): RiotChampionCatalogDto {
        val championUrl = RiotEndpoints.Champion.getChampionList(params)

        val response = httpClient.get(championUrl).body<RiotChampionCatalogDto>()

        return response
    }


    suspend fun getChampionSummaryInfo(params: Map<String, String>
    ): List<ChampionIcon> {
        // 1) 목록 URL 구성
        val listUrl = RiotEndpoints.Champion.getChampionList(params)

        // 2) GET → 3) ChampionCatalogDto로 파싱
        val catalog: RiotChampionCatalogDto = httpClient.get(listUrl).body()

        // 4) ChampionIcon 생성 (아이콘 URL은 Endpoints로 치환)
        val icons = catalog.data.values.map { dto ->
            // 안전한 아이콘 ID: "LeeSin.png" → "LeeSin"
            val championIdForIcon = dto.image.full.substringBeforeLast('.')

            // https://ddragon.../cdn/{version}/img/champion/{championId}.png
            val iconUrl = RiotEndpoints.Champion.getChampionIconUrl(
                params = mapOf(
                    "version" to catalog.version,   // 루트의 version 사용
                    "championId" to championIdForIcon
                )
            )

            ChampionIcon(
                id = dto.id,                       // "LeeSin"
                key = dto.key.toIntOrNull() ?: -1, // "64" -> 64
                name = dto.name,                   // locale 이름
                iconUrl = iconUrl
            )
        }

        // 5) 완성된 리스트 반환 (원하면 이름순 정렬)
        return icons.sortedBy { it.name }
    }

    suspend fun getChampionDetailInfo(version: String, language: String, championId: String): Map<String, Any?> {
        // 1. 챔피언 ID 기반 디테일 정보 조회
        val params = mapOf(
            "version" to version,
            "language" to language,
            "championId" to championId
        )
        val championDetailUrl = RiotEndpoints.Champion.getChampionDetailInfo(params)
        val championDetailInfo: RiotChampionDetailCatalogDto = httpClient.get(championDetailUrl).body()

        // 실제 챔피언 정보 추출
        val champion = championDetailInfo.data.values.first()


        // 2. 챔피언 아이콘 URL 생성
        val champIconUrl = RiotEndpoints.Champion.getChampionIconUrl(
            params = mapOf(
                "version" to version,
                "championId" to championId
            )
        )

        // 패시브 아이콘
        val passiveIconUrl = champion.passive?.image?.full?.let { full->
            RiotEndpoints.Champion.getPassiveIconUrl(
                params = mapOf(
                    "version" to version,
                    "championPassiveId" to full
                )
            )
        }

        // 스킬(Q, W, E, R)
        val spellsView = champion.spells.map { spell ->
            val icon = RiotEndpoints.Champion.getSpellIconUrl(
                params = mapOf(
                    "version" to version,
                    "championSpellId" to (spell.image?.full ?: "")
                )
            )
            mapOf(
                "id" to spell.id,
                "name" to spell.name,
                "tooltip" to (spell.tooltip ?: spell.description),      // tooltip(HTML 포함) 있으면 utext로 렌더, 없으면 description 사용
                "icon" to icon
            )
        }

        // 스킨
        val skinImages = champion.skins.map { skin ->
            val splash = RiotEndpoints.Champion.getSplashArtUrl(
                params = mapOf(
                    "championId" to championId,
                    "num" to skin.num.toString()
                )
            )
            mapOf("name" to (if (skin.name == "default") "기본 스킨" else skin.name), "splash" to splash)
        }

        val model = mapOf(
            "v" to version,
            "iconUrl" to champIconUrl,
            "champ" to champion,
            "passiveIcon" to passiveIconUrl,
            "spells" to spellsView,
            "skins" to skinImages
        )

        return model
    }

}