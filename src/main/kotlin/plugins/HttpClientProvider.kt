package plugins

import config.RiotHeaders
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.*

// 라이엇 GET 요청 기본 헤더
val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true   // JSON에 DTO에 없는 필드 있어도 무시
            isLenient = true           // 조금 느슨하게 파싱
            prettyPrint = true
        })
    }
    defaultRequest {
        header("User-Agent", RiotHeaders.USER_AGENT)
        header("Accept-Language", RiotHeaders.ACCEPT_LANGUAGE)
        header("Accept-Charset", RiotHeaders.ACCEPT_CHARSET)
        header("Origin", RiotHeaders.ORIGIN)
    }
}
