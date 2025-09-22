package utils

import kotlinx.serialization.json.Json

object ChampionPositionLoader {
    private val json = Json { ignoreUnknownKeys = true }
    private val positions: Map<String, List<String>> by lazy {
        val stream = this::class.java.classLoader.getResourceAsStream("config/riot_champion_position.json")
            ?: throw IllegalStateException("config/riot_champion_position.json not found")

        val text = stream.bufferedReader().use { it.readText() }
        json.decodeFromString<Map<String, List<String>>>(text)
    }

    fun getPosition(championId: String): List<String> {
        return positions[championId] ?: emptyList()
    }
}