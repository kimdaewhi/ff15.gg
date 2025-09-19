package config

import kotlinx.serialization.Serializable

@Serializable
enum class RiotRole(val displayName: String) {
    Fighter("전사"),
    Tank("탱커"),
    Assassin("암살자"),
    Marksman("원거리"),
    Mage("마법사"),
    Supporter("서포터");

    companion object {
        fun fromTag(tag: String): RiotRole? =
            entries.firstOrNull() { it.name.equals(tag, true) }
    }
}

@Serializable
enum class Position(val displayName: String) {
    Top("탑"),
    Jungle("정글"),
    Mid("미드"),
    Bottom("바텀"),
    Supporter("서포터");

    companion object {
        fun from(value: String): Position? = when (value.trim().lowercase()) {
            "Top" -> Top
            "Jungle" -> Jungle
            "Mid", "Middle" -> Mid
            "Bottom", "Bot", "Adc", "AD" -> Bottom
            "Support", "Supporter", "Sup" -> Supporter
            else -> null
        }
    }
}