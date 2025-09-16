package services

import DTO.ChampionCatalogDto
import DTO.ChampionIcon
import config.DataDragonUrl
import config.RiotEndpoints
import config.RiotEndpoints.Champion.getChampionIconUrl
import io.ktor.client.call.body
import io.ktor.client.request.get
import plugins.httpClient

object RiotChampionService {

    suspend fun getChampionCatalog(ddUrl: String, params: Map<String, String>): ChampionCatalogDto {
        val championUrl = RiotEndpoints.Champion.getChampionList(ddUrl, params)

        val response = httpClient.get(championUrl).body<ChampionCatalogDto>()

        return response
    }

    suspend fun getChampionSummaryInfo(ddUrl: String, params: Map<String, String>
    ): List<ChampionIcon> {
        // 1) 목록 URL 구성
        val listUrl = RiotEndpoints.Champion.getChampionList(ddUrl, params)

        // 2) GET → 3) ChampionCatalogDto로 파싱
        val catalog: ChampionCatalogDto = httpClient.get(listUrl).body()

        // 4) ChampionIcon 생성 (아이콘 URL은 Endpoints로 치환)
        val icons = catalog.data.values.map { dto ->
            // 안전한 아이콘 ID: "LeeSin.png" → "LeeSin"
            val championIdForIcon = dto.image.full.substringBeforeLast('.')

            // https://ddragon.../cdn/{version}/img/champion/{championId}.png
            val iconUrl = getChampionIconUrl(
                ddUrl = DataDragonUrl.CHAMPION_ICON_ASSET,
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

}