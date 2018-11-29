package asia.jokin.ohjelmistomobiili

import org.json.JSONObject
import android.content.Context

interface AlertDataCallback {
    fun onSuccess(response: ArrayList<Alert>, context: Context)
}

fun parseData(data: JSONObject): ArrayList<Alert> {
    val alerts: ArrayList<Alert> = ArrayList()
    val messages = data.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("GeneralMessageDelivery")
    for(i in 0..(messages.length() - 1)) {
        val message = messages.getJSONObject(0).getJSONArray("GeneralMessage").getJSONObject(0)
        val recordedAt = message.getLong("RecordedAtTime")
        val validUntil = message.getLong("ValidUntilTime")
        val content = message.getString("Content")
        alerts.add(Alert(recordedAt, validUntil, content))
    }
    return alerts
}