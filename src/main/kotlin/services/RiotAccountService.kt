package services

import schemas.RiotAccountResponse
import config.RiotEndpoints
import config.RiotRegion
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import plugins.httpClient

object RiotAccountService {
    suspend fun getByRiotId(region: RiotRegion, gameName: String, tagLine: String): RiotAccountResponse {
        val url = RiotEndpoints.Account.byRiotId(region, gameName, tagLine)
        val response = httpClient.get(url)
        println(response.bodyAsText())

        // return response.body<RiotAccountResponse>()
        return response.body()
    }
}
