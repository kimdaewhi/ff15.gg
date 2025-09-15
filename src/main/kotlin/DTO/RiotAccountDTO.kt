package schemas

import kotlinx.serialization.Serializable


// ⭐ Riot Spec은 접미사로 DTO 추가


@Serializable
data class RiotAccountResponseDTO(
    val puuid: String,
    val gameName: String,
    val tagLine: String
)


@Serializable
data class RiotSummonerResponseDTO(
    val puuid: String,
    val profileIconId: Int,
    val summonerLevel: Long,
    val revisionDate: Long,
)


@Serializable
data class RiotSummonerInfo(
    val puuid: String,
    val profileIconId: Int,
    val gameName: String,
    val tagLine: String,
    val summonerLevel: Long,
    val revisionDate: Long,
)