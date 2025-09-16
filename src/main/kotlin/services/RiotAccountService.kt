package services

import schemas.RiotAccountResponseDto
import config.RiotEndpoints
import config.RiotPlatform
import config.RiotRegion
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import plugins.httpClient
import schemas.*

object RiotAccountService {
    // 닉네임 + 태그 기반 소환사 기본 정보
    suspend fun getAccountInfoByRiotID(region: RiotRegion, gameName: String, tagLine: String): RiotAccountResponseDto {
        val url = RiotEndpoints.Account.getAccountInfo(region, gameName, tagLine)
        val response = httpClient.get(url)
        println(response.bodyAsText())

        return response.body()
    }


    // 닉네임 + 태그 기반 소환사 정보 조회
    suspend fun getSummonerInfoByRiotID(
        region: RiotRegion,
        platform: RiotPlatform,
        gameName: String,
        tagLine: String
    ): RiotSummonerInfo {
        // 1. 닉네임 + tagLine으로 puuid 포함한 account 정보 조회
        val accountUrl = RiotEndpoints.Account.getAccountInfo(region, gameName, tagLine)
        val accountResponse = httpClient.get(accountUrl).body<RiotAccountResponseDto>()

        // 2. puuid를 이용한 summoner 정보 조회
        val summonerUrl = RiotEndpoints.Summoner.getMySummonerInfo(
            platform,
            accountResponse.puuid
        )
        val summonerResponse = httpClient.get(summonerUrl).body<RiotSummonerResponseDto>()
        
        // 3. summonerResponse에 gameName, tagLine 추가
        return RiotSummonerInfo(
            puuid = summonerResponse.puuid,
            profileIconId = summonerResponse.profileIconId,
            gameName = accountResponse.gameName,
            tagLine = accountResponse.tagLine,
            summonerLevel = summonerResponse.summonerLevel,
            revisionDate = summonerResponse.revisionDate
        )
    }


    // 소환사 프로필 아이콘 url 생성
    fun getSummonerProfileIconUrl(
        dataDragonUrl: String,
        params: Map<String, String> = emptyMap()
    ): String {
        return RiotEndpoints.Summoner.getSummonerIconUrl(dataDragonUrl, params)
    }
}
