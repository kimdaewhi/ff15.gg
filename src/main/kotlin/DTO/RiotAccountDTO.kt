package schemas

import kotlinx.serialization.Serializable

@Serializable
data class RiotAccountResponse(
    val puuid: String,
    val gameName: String,
    val tagLine: String
)
