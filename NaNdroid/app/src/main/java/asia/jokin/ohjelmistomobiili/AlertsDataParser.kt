package asia.jokin.ohjelmistomobiili

import org.json.JSONObject
import android.content.Context
import org.json.JSONException

interface AlertDataCallback {
    fun onSuccess(response: ArrayList<Alert>, context: Context)
}

fun parseData(data: JSONObject): ArrayList<Alert> {
    val alerts: ArrayList<Alert> = ArrayList()
    try {
        val messages = data.getJSONObject("Siri").getJSONObject("ServiceDelivery").getJSONArray("GeneralMessageDelivery")
        for(i in 0..(messages.length() - 1)) {
            val message = messages.getJSONObject(0).getJSONArray("GeneralMessage").getJSONObject(0)
            val recordedAt = message.getLong("RecordedAtTime")
            val validUntil = message.getLong("ValidUntilTime")
            val content = message.getString("Content")
            alerts.add(Alert(recordedAt, validUntil, content))
        }
    }
    catch (e: JSONException) {
        println("Empty response, return empty array")
    }

    return alerts
}