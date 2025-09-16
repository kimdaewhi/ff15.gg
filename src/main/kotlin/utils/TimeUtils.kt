package utils

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/** epoch millis → "n시간 전" 같은 사람이 읽기 쉬운 문자열 */
fun humanizeAgo(epochMillis: Long, nowMillis: Long = System.currentTimeMillis()): String {
    val ago: Duration = (nowMillis - epochMillis).milliseconds
    return when {
        ago.inWholeDays >= 1    -> "${ago.inWholeDays}일 전"
        ago.inWholeHours >= 1   -> "${ago.inWholeHours}시간 전"
        ago.inWholeMinutes >= 1 -> "${ago.inWholeMinutes}분 전"
        else                    -> "방금 전"
    }
}
