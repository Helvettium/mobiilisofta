package asia.jokin.ohjelmistomobiili

import java.io.Serializable

data class Alert(val recordedAt: Long, val validUntil: Long, val content: String): Serializable