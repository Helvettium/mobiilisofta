package asia.jokin.ohjelmistomobiili

import java.util.Date

data class Alert(val recordedAt: Long, val validUntil: Long, val content: String)