package services

import schemas.RiotAccountResponseDTO
import config.RiotEndpoints
import config.RiotPlatform
import config.RiotRegion
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import plugins.httpClient
import schemas.*

object RiotAccountService {
    suspend fun getAccountInfoByRiotID(region: RiotRegion, gameName: String, tagLine: String): RiotAccountResponseDTO {
        val url = RiotEndpoints.Account.getAccountInfo(region, gameName, tagLine)
        val response = httpClient.get(url)
        println(response.bodyAsText())

        return response.body()
    }


    suspend fun getSummonerInfoByRiotID(
        region: RiotRegion,
        platform: RiotPlatform,
        gameName: String,
        tagLine: String
    ): RiotSummonerInfo {
        // 1. 닉네임 + tagLine으로 puuid 포함한 account 정보 조회
        val accountUrl = RiotEndpoints.Account.getAccountInfo(region, gameName, tagLine)
        val accountResponse = httpClient.get(accountUrl).body<RiotAccountResponseDTO>()

        // 2. puuid를 이용한 summoner 정보 조회
        val summonerUrl = RiotEndpoints.Summoner.getMySummonerInfo(
            platform,
            accountResponse.puuid
        )
        val summonerResponse = httpClient.get(summonerUrl).body<RiotSummonerResponseDTO>()
        
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
}
