package DTO

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


// ⭐ Riot Spec은 접미사로 DTO 추가


// 챔피언 개요 DTO
@Serializable
data class RiotChampionInfoDto(
    val attack: Int,                        // 피해 성향
    val defense: Int,                       // 방어
    val magic: Int,                         // 마법(?)
    val difficulty: Int                     // 난이도
)

// 챔피언 스탯 관련 DTO
@Serializable
data class RiotChampionStatsDto(
    val hp: Double,                         // 체력
    val hpperlevel: Double,                 // 체력 - 레벨당 상승
    val mp: Double,                         // 자원
    val mpperlevel: Double,                 // 자원 - 레벨당 상승
    val movespeed: Double,                  // 이동속도
    val armor: Double,                      // 방어력
    val armorperlevel: Double,              // 방어력 - 레벨당 상승
    val spellblock: Double,                 // 마법저항력
    val spellblockperlevel: Double,         // 마법저항력 - 레벨당 상승
    val attackrange: Double,                // 사거리
    val hpregen: Double,                    // 체력 재생
    val hpregenperlevel: Double,            // 체력 재생 - 레벨당 상승
    val mpregen: Double,                    // 마나 재생
    val mpregenperlevel: Double,            // 마나 재생 - 레벨당 상승
    val crit: Double,                       // 크리티컬 확률
    val critperlevel: Double,               // 크리티컬 확률 - 레벨당 상승
    val attackdamage: Double,               // 공격력
    val attackdamageperlevel: Double,       // 공격력 - 레벨당 상승
    val attackspeedperlevel: Double,        // 공격속도
    val attackspeed: Double                 // 공격속도 - 레벨당 상승
)


// 챔피언 이미지 관련 DTO
@Serializable
data class RiotChampionImageDto(
    val full: String,    // "Jinx.png"
    val sprite: String,  // "champion1.png"
    val group: String,   // "champion"
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int
)


// 챔피언 전체 정보 DTO
@Serializable
data class RiotChampionDto(
    val version: String,                    // 버전
    val id: String,                         // 챔피언 ID(ex. Jinx)
    val key: String,                        // 챔피언 고유 key(ex. 64)
    val name: String,                       // 챔피언 이름(ex. 징크스)
    val title: String,                      // 타이틀
    val blurb: String? = null,              // 소개글
    val info: RiotChampionInfoDto,              // 개요
    val image: RiotChampionImageDto,            // 이미지
    val tags: List<String> = emptyList(),   // 역할군(ex. Fighter, Assassin...) 선두컬럼 제외 나머지 부역할군
    val partype: String? = null,            // 코스트 종류(locale 종속)
    val stats: RiotChampionStatsDto             // 스탯
)

// champion.json 루트 객체 DTO
@Serializable
data class RiotChampionCatalogDto(
    val type: String,                  // 리소스 타입
    val format: String,                // 포맷 지시자(대부분 standAloneComplex)
    val version: String,               // 버전
    val data: Map<String, RiotChampionDto> // key: "Aatrox", "Jinx" ...
)


// 챔피언 상세 정보(champion/Aatrox.json) 루트 객체 DTO
@Serializable
data class RiotChampionDetailCatalogDto(
    val type: String,
    val format: String,
    val version: String,
    val data: Map<String, RiotChampionDetailDto> // 보통 1개만 들어있음
)


// 챔피언 상세 정보
@Serializable
data class RiotChampionDetailDto(
    val id: String,
    val key: String,
    val name: String,
    val title: String,
    val image: RiotChampionImageDto,
    val skins: List<RiotSkinDto> = emptyList(),
    val lore: String? = null,
    val blurb: String? = null,
    val allytips: List<String> = emptyList(),
    val enemytips: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val partype: String? = null,
    val info: RiotChampionInfoDto? = null,
    val stats: RiotChampionStatsDto? = null,
    val spells: List<RiotSpellDto> = emptyList(),
    val passive: RiotPassiveDto? = null,
    val recommended: JsonElement? = null // 안 쓰면 남겨두기
)


// 챔피언 스킨 정보
@Serializable
data class RiotSkinDto(
    val id: String,     // "222001"
    val num: Int,       // 1
    val name: String,   // "범죄 도시 징크스" (default면 "default")
    val chromas: Boolean = false
)


// 챔피언 스킬셋
@Serializable
data class RiotSpellDto(
    val id: String,
    val name: String,
    val description: String? = null, // HTML 포함
    val tooltip: String? = null,     // HTML 포함
    val leveltip: JsonElement? = null,
    val maxrank: Int? = null,
    val cooldown: List<Double> = emptyList(),
    val cooldownBurn: String? = null,
    val cost: List<Int> = emptyList(),
    val costBurn: String? = null,
    val datavalues: JsonElement? = null,
    val effect: JsonElement? = null,     // [null, [..], ...] 형태
    val effectBurn: JsonElement? = null,
    val vars: JsonElement? = null,
    val costType: String? = null,
    val maxammo: String? = null,
    // val range: List<Int> = emptyList(),
    val range: List<Long>? = emptyList(),
    val rangeBurn: String? = null,
    val image: RiotChampionImageDto? = null, // group: "spell"
    val resource: String? = null
)


// 챔피언 패시브
@Serializable
data class RiotPassiveDto(
    val name: String,
    val description: String? = null,  // HTML 포함
    val image: RiotChampionImageDto       // group: "passive"
)
/* ======================================== Customized DTO ======================================== */


// 챔피언 요약 정보
@Serializable
data class ChampionSummary(
    val id: String,          // 챔피언 ID(Jinx)
    val key: Int,            // 222 (DDragon의 key는 문자열이므로 toInt)
    val name: String,        // 이름(locale 적용)
    val iconUrl: String,     // 챔피언 아이콘
    val position: String     // 포지션
)