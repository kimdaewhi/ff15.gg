package config

// 라이엇 정의 지역
enum class RiotRegion(val baseUrl: String = "asia.api.riotgames.com") {
    AMERICAS("americas.api.riotgames.com"),
    ASIA("asia.api.riotgames.com"),
    EUROPE("europe.api.riotgames.com"),
    SEA("sea.api.riotgames.com")
}


// 라이엇 정의 플랫폼
enum class RiotPlatform(val baseUrl: String = "kr.api.riotgames.com") {
    BR1("br1.api.riotgames.com"),
    EUN1("eun1.api.riotgames.com"),
    EUW1("euw1.api.riotgames.com"),
    JP1("jp1.api.riotgames.com"),
    KR("kr.api.riotgames.com"),
    LA1("la1.api.riotgames.com"),
    LA2("la2.api.riotgames.com"),

    NA1("na1.api.riotgames.com"),
    OC1("oc1.api.riotgames.com"),
    TR1("tr1.api.riotgames.com"),
    RU("ru.api.riotgames.com"),
    PH2("ph2.api.riotgames.com"),
    SG2("sg2.api.riotgames.com"),

    TH2("th2.api.riotgames.com"),
    TW2("tw2.api.riotgames.com"),
    VN2("vn2.api.riotgames.com")
}


// 라이엇 언어
enum class RiotLanguage(val code: String) {
    CS_CZ("cs_CZ"),   // Czech
    EL_GR("el_GR"),   // Greek
    PL_PL("pl_PL"),   // Polish
    RO_RO("ro_RO"),   // Romanian
    HU_HU("hu_HU"),   // Hungarian
    EN_GB("en_GB"),   // English (UK)
    DE_DE("de_DE"),   // German
    ES_ES("es_ES"),   // Spanish (Spain)
    IT_IT("it_IT"),   // Italian
    FR_FR("fr_FR"),   // French
    JA_JP("ja_JP"),   // Japanese
    KO_KR("ko_KR"),   // Korean
    ES_MX("es_MX"),   // Spanish (Mexico)
    ES_AR("es_AR"),   // Spanish (Argentina)
    PT_BR("pt_BR"),   // Portuguese (Brazil)
    EN_US("en_US"),   // English (US)
    EN_AU("en_AU"),   // English (Australia)
    RU_RU("ru_RU"),   // Russian
    TR_TR("tr_TR"),   // Turkish
    MS_MY("ms_MY"),   // Malay
    EN_PH("en_PH"),   // English (Philippines)
    EN_SG("en_SG"),   // English (Singapore)
    TH_TH("th_TH"),   // Thai
    VI_VN("vi_VN"),   // Vietnamese
    ID_ID("id_ID"),   // Indonesian
    ZH_MY("zh_MY"),   // Chinese (Malaysia)
    ZH_CN("zh_CN"),   // Chinese (China)
    ZH_TW("zh_TW");   // Chinese (Taiwan)
}

// 라이엇 엔드포인트 정의
object RiotEndpointUrl {
    // Account
    const val ACCOUNT_BY_RIOT_ID = "/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}?api_key={apiKey}"


    // Summoner
    const val SUMMONER_BY_PUUID = "/lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}?api_key={apiKey}"
}


// GET 요청 파라미터 파싱
fun buildUrl(
    regionOrPlatform: String,
    template: String,
    params: Map<String, String> = emptyMap()
): String {
    var url = "https://$regionOrPlatform$template"

    //  일반 파라미터 파싱
    params.forEach { (key, value) ->
        url = url.replace("{$key}", value)
    }

    // 전역 API Key 치환
    url = url.replace("{apiKey}", RiotGlobals.apiKey)

    return url
}




object RiotEndpoints {
    object Account {
        fun getAccountInfo(region: RiotRegion, gameName: String, tagLine: String): String =
            buildUrl(region.baseUrl, RiotEndpointUrl.ACCOUNT_BY_RIOT_ID,
                mapOf("gameName" to gameName, "tagLine" to tagLine))

    }


    object Summoner {
        fun getMySummonerInfo(platform: RiotPlatform, encryptedPUUID: String): String =
            buildUrl(
                platform.baseUrl,
                RiotEndpointUrl.SUMMONER_BY_PUUID,
                mapOf("encryptedPUUID" to encryptedPUUID)
            )
    }
}